package onlinestockbrokeragesystem_sde3.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Value object representing a Limit Order. 
 * Thread-safe: All state mutations are handled functionally or by the exact owner thread.
 */
public class Order implements Comparable<Order> {
    private final String id;
    private final String accountId;
    private final String symbol;
    private final OrderSide side;
    private final BigDecimal limitPrice;
    private int remainingQuantity;
    private OrderStatus status;
    private final Instant timestamp;

    public Order(String accountId, String symbol, OrderSide side, int quantity, BigDecimal limitPrice) {
        this.id = UUID.randomUUID().toString();
        this.accountId = accountId;
        this.symbol = symbol;
        this.side = side;
        this.remainingQuantity = quantity;
        this.limitPrice = limitPrice;
        this.status = OrderStatus.PENDING;
        this.timestamp = Instant.now();
    }

    public void fill(int filledQty) {
        this.remainingQuantity -= filledQty;
        this.status = (remainingQuantity == 0) ? OrderStatus.FILLED : OrderStatus.PARTIALLY_FILLED;
    }

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

    public String getId() { return id; }
    public String getAccountId() { return accountId; }
    public String getSymbol() { return symbol; }
    public OrderSide getSide() { return side; }
    public BigDecimal getLimitPrice() { return limitPrice; }
    public int getRemainingQuantity() { return remainingQuantity; }
    public OrderStatus getStatus() { return status; }
    public Instant getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return "Order{" + id.substring(0,8) + " " + side + " " + remainingQuantity + " " + symbol + " @ $" + limitPrice + "}";
    }
}
