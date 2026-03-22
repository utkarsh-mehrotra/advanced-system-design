package onlinestockbrokeragesystem_sde3.service;

import onlinestockbrokeragesystem_sde3.messaging.MessageBus;
import onlinestockbrokeragesystem_sde3.model.Order;
import onlinestockbrokeragesystem_sde3.model.OrderSide;
import onlinestockbrokeragesystem_sde3.model.OrderStatus;
import onlinestockbrokeragesystem_sde3.model.Trade;
import onlinestockbrokeragesystem_sde3.model.events.OrderCreatedEvent;
import onlinestockbrokeragesystem_sde3.model.events.TradeExecutedEvent;

import java.math.BigDecimal;
import java.util.PriorityQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * SDE3 Distributed Stateful Matching Engine.
 * One Thread per Symbol partition.
 * NO LOCKS inside the matching core because only the single consumer thread ever edits the PriorityQueues.
 */
public class MatchingEngineService {
    private final String symbol;
    private final PriorityQueue<Order> buyBook = new PriorityQueue<>();
    private final PriorityQueue<Order> sellBook = new PriorityQueue<>();
    private final ExecutorService matchingThread;

    public MatchingEngineService(String symbol) {
        this.symbol = symbol;
        this.matchingThread = Executors.newSingleThreadExecutor();
        startEventLoop();
    }

    private void startEventLoop() {
        matchingThread.submit(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    OrderCreatedEvent event = MessageBus.getInstance().subscribeToOrderPartition(symbol).take();
                    processNewOrderLocally(event.getOrder());
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }

    /** No ReentrantLocks! Only the matchingThread runs this logic for a given symbol. */
    private void processNewOrderLocally(Order order) {
        if (order.getSide() == OrderSide.BUY) {
            buyBook.offer(order);
        } else {
            sellBook.offer(order);
        }
        matchOrders();
    }

    private void matchOrders() {
        while (!buyBook.isEmpty() && !sellBook.isEmpty()) {
            Order topBid = buyBook.peek();
            Order topAsk = sellBook.peek();

            // No overlap in prices
            if (topBid.getLimitPrice().compareTo(topAsk.getLimitPrice()) < 0) {
                break;
            }

            int matchQuantity = Math.min(topBid.getRemainingQuantity(), topAsk.getRemainingQuantity());
            // In continuous trading, price goes to the maker (the resting order).
            // We use topAsk price if the buy order crossed the existing ask.
            BigDecimal matchPrice = topAsk.getTimestamp().isBefore(topBid.getTimestamp()) ? 
                                    topAsk.getLimitPrice() : topBid.getLimitPrice();

            topBid.fill(matchQuantity);
            topAsk.fill(matchQuantity);

            // Publish Trade to the bus asynchronously
            Trade trade = new Trade(topBid, topAsk, symbol, matchQuantity, matchPrice);
            MessageBus.getInstance().publishTrade(new TradeExecutedEvent(trade));

            System.out.println("[MATCH ENGINE] Executed " + trade);

            if (topBid.getStatus() == OrderStatus.FILLED) buyBook.poll();
            if (topAsk.getStatus() == OrderStatus.FILLED) sellBook.poll();
        }
    }
    
    public void shutdown() {
        matchingThread.shutdownNow();
    }
}
