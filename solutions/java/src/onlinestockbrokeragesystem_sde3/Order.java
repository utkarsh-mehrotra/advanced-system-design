package onlinestockbrokeragesystem_sde3;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * SDE3: Limit Order entity with BigDecimal price. Implements Comparable for PriorityQueue ordering.
 */
public class Order implements Comparable<Order> {
    private final String id;
    private final Account account;
    private final Portfolio portfolio;
    private final String symbol;
    private final OrderSide side;
    private final BigDecimal limitPrice;
    private int remainingQuantity;
    private OrderStatus status;
    private final Instant timestamp;

    public Order(Account account, Portfolio portfolio, String symbol, OrderSide side, int quantity, BigDecimal limitPrice) {
        this.id = UUID.randomUUID().toString();
        this.account = account;
        this.portfolio = portfolio;
        this.symbol = symbol;
        this.side = side;
        this.remainingQuantity = quantity;
        this.limitPrice = limitPrice;
        this.status = OrderStatus.PENDING;
        this.timestamp = Instant.now();
    }

    /**
     * For BUY orders in a Max-Heap: higher bid price = higher priority.
     * For SELL orders in a Min-Heap: lower ask price = higher priority.
     * Tie-break by timestamp (older order first = FIFO).
     */
    @Override
    public int compareTo(Order other) {
        if (this.side == OrderSide.BUY) {
            // Max-Heap: negate comparison so highest bid is polled first
            int priceCompare = other.limitPrice.compareTo(this.limitPrice);
            return priceCompare != 0 ? priceCompare : this.timestamp.compareTo(other.timestamp);
        } else {
            // Min-Heap: lowest ask price polled first
            int priceCompare = this.limitPrice.compareTo(other.limitPrice);
            return priceCompare != 0 ? priceCompare : this.timestamp.compareTo(other.timestamp);
        }
    }

    public void fill(int filledQty) {
        this.remainingQuantity -= filledQty;
        this.status = (remainingQuantity == 0) ? OrderStatus.FILLED : OrderStatus.PARTIALLY_FILLED;
    }

    public String getId() { return id; }
    public Account getAccount() { return account; }
    public Portfolio getPortfolio() { return portfolio; }
    public String getSymbol() { return symbol; }
    public OrderSide getSide() { return side; }
    public BigDecimal getLimitPrice() { return limitPrice; }
    public int getRemainingQuantity() { return remainingQuantity; }
    public OrderStatus getStatus() { return status; }
}
