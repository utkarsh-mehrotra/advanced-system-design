package onlinestockbrokeragesystem_upgraded;

import java.math.BigDecimal;
import java.util.PriorityQueue;
import java.util.concurrent.locks.ReentrantLock;

/**
 * SDE3: The core Wall Street clearing-house mechanism.
 * Maintains two PriorityQueues per symbol:
 *   - BUY book  → Max-Heap (highest bid matched first)
 *   - SELL book → Min-Heap (lowest ask matched first)
 * A trade executes when: max(bids).price >= min(asks).price
 */
public class OrderMatchingEngine {
    private final String symbol;

    // Max-Heap for buy orders (highest price = highest priority)
    private final PriorityQueue<Order> buyBook = new PriorityQueue<>();
    // Min-Heap for sell orders (lowest price = highest priority)
    private final PriorityQueue<Order> sellBook = new PriorityQueue<>();

    // Single lock per order book (per symbol), not a global lock
    private final ReentrantLock bookLock = new ReentrantLock();

    public OrderMatchingEngine(String symbol) {
        this.symbol = symbol;
    }

    public void submitOrder(Order order) {
        bookLock.lock();
        try {
            if (order.getSide() == OrderSide.BUY) {
                buyBook.offer(order);
            } else {
                sellBook.offer(order);
            }
            matchOrders();
        } finally {
            bookLock.unlock();
        }
    }

    /** Must be called holding bookLock */
    private void matchOrders() {
        while (!buyBook.isEmpty() && !sellBook.isEmpty()) {
            Order topBid = buyBook.peek();
            Order topAsk = sellBook.peek();

            // Crossing condition: bid price >= ask price
            if (topBid.getLimitPrice().compareTo(topAsk.getLimitPrice()) < 0) {
                break; // No match possible, books are sorted
            }

            // Trade executes at the ask price (price-time priority)
            BigDecimal tradePrice = topAsk.getLimitPrice();
            int tradeQty = Math.min(topBid.getRemainingQuantity(), topAsk.getRemainingQuantity());

            BigDecimal tradeCost = tradePrice.multiply(BigDecimal.valueOf(tradeQty));

            // Debit buyer's cash
            boolean funded = topBid.getAccount().debitFunds(tradeCost);
            if (!funded) {
                buyBook.poll(); // Remove unfunded order
                System.out.println("[OrderBook] BUY order rejected — insufficient funds.");
                continue;
            }

            // Debit seller's position
            boolean hasStock = topAsk.getPortfolio().removePosition(symbol, tradeQty);
            if (!hasStock) {
                sellBook.poll(); // Remove invalid sell order
                topBid.getAccount().creditFunds(tradeCost); // Refund buyer
                System.out.println("[OrderBook] SELL order rejected — no position.");
                continue;
            }

            // Settle the trade
            topBid.fill(tradeQty);
            topAsk.fill(tradeQty);

            topAsk.getAccount().creditFunds(tradeCost);
            topBid.getPortfolio().addPosition(symbol, tradeQty);

            System.out.printf("[TRADE EXECUTED] %s: %d shares @ $%s | Buyer: %s | Seller: %s%n",
                    symbol, tradeQty, tradePrice.toPlainString(),
                    topBid.getAccount().getUser().getName(),
                    topAsk.getAccount().getUser().getName());

            // Remove fully-filled orders from top of heap
            if (topBid.getStatus() == OrderStatus.FILLED) buyBook.poll();
            if (topAsk.getStatus() == OrderStatus.FILLED) sellBook.poll();
        }
    }

    public int getBuyDepth() { bookLock.lock(); try { return buyBook.size(); } finally { bookLock.unlock(); } }
    public int getSellDepth() { bookLock.lock(); try { return sellBook.size(); } finally { bookLock.unlock(); } }
}
