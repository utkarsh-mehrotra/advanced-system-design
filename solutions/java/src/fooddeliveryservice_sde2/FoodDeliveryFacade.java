package fooddeliveryservice_sde2;

import fooddeliveryservice_sde2.order.Order;
import fooddeliveryservice_sde2.order.OrderItem;
import fooddeliveryservice_sde2.order.OrderStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Replaces the God-Singleton class.
 * Orchestrates ConcurrentHashMaps and explicitly handles Strategy & Notification routing externally.
 */
public class FoodDeliveryFacade {
    private final Map<String, Customer> customers;
    private final Map<String, Restaurant> restaurants;
    private final Map<String, Order> orders;
    
    // Concurrency Note: We use CopyOnWriteArrayList because iterating Drivers is the most common read op!
    private final List<DeliveryAgent> deliveryAgents;

    private final DeliveryMatchingStrategy matchingStrategy;
    private final NotificationDispatcher notificationDispatcher;

    public FoodDeliveryFacade(DeliveryMatchingStrategy matchingStrategy) {
        this.customers = new ConcurrentHashMap<>();
        this.restaurants = new ConcurrentHashMap<>();
        this.orders = new ConcurrentHashMap<>();
        this.deliveryAgents = new CopyOnWriteArrayList<>();
        
        // Injected behaviors
        this.matchingStrategy = matchingStrategy;
        this.notificationDispatcher = new NotificationDispatcher();
    }

    public void registerCustomer(Customer customer) { customers.put(customer.getId(), customer); }
    public void registerRestaurant(Restaurant restaurant) { restaurants.put(restaurant.getId(), restaurant); }
    public void registerDeliveryAgent(DeliveryAgent agent) { deliveryAgents.add(agent); }

    public Order placeOrder(String customerId, String restaurantId, List<OrderItem> items) {
        Customer customer = customers.get(customerId);
        Restaurant restaurant = restaurants.get(restaurantId);
        if (customer != null && restaurant != null) {
            String trackingId = "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            Order order = new Order(trackingId, customer, restaurant, items);
            orders.put(order.getId(), order);
            
            // Asynchronous Fire-and-Forget Notification!
            notificationDispatcher.dispatchRestaurantNotification(restaurant.getName(), "New incoming ticket: " + trackingId);
            return order;
        }
        return null; // Reject
    }

    public void updateOrderStatus(String orderId, OrderStatus status) {
        Order order = orders.get(orderId);
        if (order != null) {
            order.setStatus(status);
            
            // Background Notify User
            notificationDispatcher.dispatchCustomerNotification("Order " + orderId + " progressed to " + status);
            
            // SDE3: TOCTOU Driver Isolation Logic!
            if (status == OrderStatus.CONFIRMED) {
                // The strategy uses the lockAssignment (CAS instruction) under the hood. 
                // We are mathematically guaranteed not to steal another order's driver!
                DeliveryAgent assignedAgent = matchingStrategy.findAndLockOptimalAgent(deliveryAgents, order);
                
                if (assignedAgent != null) {
                    order.setDeliveryAgent(assignedAgent);
                    notificationDispatcher.dispatchDriverNotification(assignedAgent.getName(), "You have been exclusively locked to order " + orderId);
                } else {
                    notificationDispatcher.dispatchCustomerNotification("Warning: High Demand. No Drivers currently available to lock.");
                }
            }
        }
    }

    public void cancelOrder(String orderId) {
        Order order = orders.get(orderId);
        if (order != null && order.getStatus() == OrderStatus.PENDING) {
            order.setStatus(OrderStatus.CANCELLED);
            notificationDispatcher.dispatchRestaurantNotification(order.getRestaurant().getName(), "Order " + orderId + " gracefully cancelled.");
            notificationDispatcher.dispatchCustomerNotification("Your order " + orderId + " cancellation was processed.");
            
            DeliveryAgent assigned = order.getDeliveryAgent();
            if (assigned != null) {
                assigned.releaseAssignment();
                notificationDispatcher.dispatchDriverNotification(assigned.getName(), "Order " + orderId + " cancelled. You are unlocked.");
            }
        }
    }
    
    public void shutdownNetwork() {
        notificationDispatcher.shutdownTaskPool();
    }
}
