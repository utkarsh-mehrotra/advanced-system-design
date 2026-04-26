package onlineshopping_sde3;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Order {
    private final String id;
    private final User user;
    private final List<OrderItem> items;
    private final double totalAmount;
    private final AtomicReference<OrderStatus> status;

    public Order(String id, User user, List<OrderItem> items) {
        this.id = id;
        this.user = user;
        this.items = List.copyOf(items); // Immutable copy
        this.totalAmount = calculateTotalAmount();
        this.status = new AtomicReference<>(OrderStatus.PENDING);
    }

    private double calculateTotalAmount() {
        return items.stream().mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity()).sum();
    }

    public void setStatus(OrderStatus newStatus) {
        this.status.set(newStatus);
    }

    public String getId() { return id; }
    public User getUser() { return user; }
    public List<OrderItem> getItems() { return items; }
    public double getTotalAmount() { return totalAmount; }
    public OrderStatus getStatus() { return status.get(); }
}
