package fooddeliveryservice_sde3;

import java.util.concurrent.atomic.AtomicReference;

public class Order {
    private final String orderId;
    private final AtomicReference<OrderStatus> status;

    public Order(String orderId) {
        this.orderId = orderId;
        this.status = new AtomicReference<>(OrderStatus.PLACED);
    }

    public String getOrderId() { return orderId; }
    public OrderStatus getStatus() { return status.get(); }

    public boolean transitionStatus(OrderStatus expected, OrderStatus next) {
        if (status.compareAndSet(expected, next)) {
            // Decouple external triggers (e.g. searching for Delivery Agent) from Lock success
            EventBus.getInstance().publish("ORDER_STATUS_CHANGED", orderId + ":" + next.name());
            return true;
        }
        return false;
    }
}
