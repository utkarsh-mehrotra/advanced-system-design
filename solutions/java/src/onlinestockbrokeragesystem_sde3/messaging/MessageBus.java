package onlinestockbrokeragesystem_sde3.messaging;

import onlinestockbrokeragesystem_sde3.model.events.OrderCreatedEvent;
import onlinestockbrokeragesystem_sde3.model.events.TradeExecutedEvent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Simulates a partitioned Kafka cluster or message broker.
 * Orders are partitioned by symbol.
 * Trades are single-partition for the Ledger consumer.
 */
public class MessageBus {
    private static final MessageBus INSTANCE = new MessageBus();
    
    // orders.inbound topics partitioned by symbol
    private final ConcurrentHashMap<String, BlockingQueue<OrderCreatedEvent>> orderTopicsPartitionedBySymbol = new ConcurrentHashMap<>();
    
    // trades.outbound topic for ledger processing
    private final BlockingQueue<TradeExecutedEvent> tradesOutbound = new LinkedBlockingQueue<>();

    private MessageBus() {}

    public static MessageBus getInstance() {
        return INSTANCE;
    }

    public void publishOrder(OrderCreatedEvent event) {
        String symbol = event.getOrder().getSymbol();
        orderTopicsPartitionedBySymbol.computeIfAbsent(symbol, k -> new LinkedBlockingQueue<>()).offer(event);
    }

    public BlockingQueue<OrderCreatedEvent> subscribeToOrderPartition(String symbol) {
        return orderTopicsPartitionedBySymbol.computeIfAbsent(symbol, k -> new LinkedBlockingQueue<>());
    }

    public void publishTrade(TradeExecutedEvent event) {
        tradesOutbound.offer(event);
    }

    public BlockingQueue<TradeExecutedEvent> subscribeToTradesOutbound() {
        return tradesOutbound;
    }
}
