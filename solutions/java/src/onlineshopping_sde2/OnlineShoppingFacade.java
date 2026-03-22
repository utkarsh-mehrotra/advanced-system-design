package onlineshopping_sde2;

import onlineshopping_sde2.payment.Payment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class OnlineShoppingFacade {
    private final Map<String, User> users;
    private final Map<String, Product> products;
    private final Map<String, Order> orders;

    public OnlineShoppingFacade() {
        this.users = new ConcurrentHashMap<>();
        this.products = new ConcurrentHashMap<>();
        this.orders = new ConcurrentHashMap<>();
    }

    public void registerUser(User user) {
        users.put(user.getId(), user);
    }

    public void addProduct(Product product) {
        products.put(product.getId(), product);
    }

    public List<Product> searchProducts(String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        return products.values().parallelStream()
                .filter(p -> p.getName().toLowerCase().contains(lowerKeyword) ||
                             p.getDescription().toLowerCase().contains(lowerKeyword))
                .collect(Collectors.toList());
    }

    /**
     * Saga pattern implementation for atomic order placement.
     * Attempts to deduct stock from all items. If any item fails (OOS),
     * it rolls back deductions for successful items.
     * Then attempts payment. If payment fails, it rolls back all deductions.
     */
    public Order placeOrder(User user, ShoppingCart cart, Payment payment) {
        List<OrderItem> orderItems = cart.getItems();
        if (orderItems.isEmpty()) {
            throw new IllegalStateException("Cannot place order. Shopping cart is empty.");
        }

        List<OrderItem> successfullyDeductedItems = new ArrayList<>();
        boolean deductionSuccessful = true;

        // Phase 1: Try deduct inventory incrementally (Saga Step 1)
        for (OrderItem item : orderItems) {
            Product product = item.getProduct();
            boolean success = product.tryDeductQuantity(item.getQuantity());
            if (success) {
                successfullyDeductedItems.add(item);
            } else {
                deductionSuccessful = false;
                System.out.println("Out of stock for product: " + product.getName() + " (Requested: " + item.getQuantity() + ")");
                break;
            }
        }

        // Phase 1.5: Rollback if stock deduction failed (Compensating Transaction)
        if (!deductionSuccessful) {
            System.out.println("SAGA ROLLBACK: Reverting perfectly claimed stock due to partial out-of-stock.");
            for (OrderItem item : successfullyDeductedItems) {
                item.getProduct().restoreQuantity(item.getQuantity());
            }
            throw new IllegalStateException("Order partially failed due to stock out. Cart not processed.");
        }

        // Proceed to generate order now that inventory is locked
        String orderId = generateOrderId();
        Order order = new Order(orderId, user, orderItems);
        
        // Phase 2: Payment (Saga Step 2)
        if (payment.processPayment(order.getTotalAmount())) {
            order.setStatus(OrderStatus.PROCESSING);
            orders.put(orderId, order);
            user.addOrder(order);
            cart.clear(); // Only clear cart on absolute success
            return order;
        } else {
            // Phase 2.5: Rollback if payment failed (Compensating Transaction)
            System.out.println("SAGA ROLLBACK: Reverting claimed stock due to PAYMENT FAILURE.");
            for (OrderItem item : successfullyDeductedItems) {
                item.getProduct().restoreQuantity(item.getQuantity());
            }
            order.setStatus(OrderStatus.CANCELLED);
            orders.put(orderId, order);
            user.addOrder(order);
            throw new IllegalStateException("Payment failed. Order cancelled and stock reverted.");
        }
    }

    public Order getOrder(String orderId) {
        return orders.get(orderId);
    }

    private String generateOrderId() {
        return "ORDER" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
