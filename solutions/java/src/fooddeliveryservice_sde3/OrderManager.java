package fooddeliveryservice_sde3;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OrderManager {
    private final Map<String, Order> activeOrders = new ConcurrentHashMap<>();

    public void createOrder(Order order) {
        activeOrders.put(order.getOrderId(), order);
        EventBus.getInstance().publish("NEW_ORDER_PLACED", order.getOrderId());
    }

    public void updateOrderStatus(String orderId, OrderStatus expected, OrderStatus next) {
        Order order = activeOrders.get(orderId);
        if (order != null) {
            boolean success = order.transitionStatus(expected, next);
            if (!success) {
                System.out.println("OrderManager: Invalid Transition. Interleaved state change occurred for " + orderId);
            }
        }
    }
}
