package fooddeliveryservice_upgraded.order;

import fooddeliveryservice_upgraded.Customer;
import fooddeliveryservice_upgraded.DeliveryAgent;
import fooddeliveryservice_upgraded.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private final String id;
    private final Customer customer;
    private final Restaurant restaurant;
    private final List<OrderItem> items;
    private volatile OrderStatus status; // Volatile for thread visibility since Observers read it asynchronously
    private DeliveryAgent deliveryAgent;

    public Order(String id, Customer customer, Restaurant restaurant, List<OrderItem> items) {
        this.id = id;
        this.customer = customer;
        this.restaurant = restaurant;
        this.items = new ArrayList<>(items);
        this.status = OrderStatus.PENDING;
    }

    public synchronized void setStatus(OrderStatus status) {
        this.status = status;
    }

    public synchronized void setDeliveryAgent(DeliveryAgent agent) {
        this.deliveryAgent = agent;
    }

    public String getId() { return id; }
    public Customer getCustomer() { return customer; }
    public Restaurant getRestaurant() { return restaurant; }
    public OrderStatus getStatus() { return status; }
    public DeliveryAgent getDeliveryAgent() { return deliveryAgent; }
    public List<OrderItem> getItems() { return items; }
}
