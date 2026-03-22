package restaurantmanagementsystem_sde2;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private final int id;
    private final List<MenuItem> items;
    
    // SDE3: Critical finance structural fix from Double to BigDecimal
    private final BigDecimal totalAmount;
    private OrderStatus status;
    private final LocalDateTime timestamp;

    public Order(int id, List<MenuItem> items, BigDecimal totalAmount, OrderStatus status) {
        this.id = id;
        this.items = items;
        this.totalAmount = totalAmount;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }

    public void setStatus(OrderStatus status) { this.status = status; }

    public int getId() { return id; }
    public List<MenuItem> getItems() { return items; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public OrderStatus getStatus() { return status; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
