package onlineshopping_sde3;

import onlineshopping_sde3.payment.CreditCardPayment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class OnlineShoppingServiceDemoUpgraded {
    public static void run() {
        OnlineShoppingFacade shoppingService = new OnlineShoppingFacade();

        // Register users
        User user1 = new User("U001", "John Doe", "john@example.com", "password123");
        shoppingService.registerUser(user1);

        // Add products. We only have 10 High-end smartphones.
        Product product1 = new Product("P001", "Smartphone", "High-end smartphone", 1000.00, 10);
        Product product2 = new Product("P002", "Laptop", "Powerful laptop", 2000.00, 100);
        shoppingService.addProduct(product1);
        shoppingService.addProduct(product2);

        System.out.println("--- Starting concurrent flash sale for limited supply smartphone ---");
        System.out.println("Initial Smartphone Inventory: " + product1.getQuantity());

        // Test concurrent flash sale (20 threads trying to buy 1 smartphone each. Only 10 should succeed)
        int numThreads = 20;
        ExecutorService executor = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(numThreads);
        
        AtomicInteger successfulOrders = new AtomicInteger(0);
        AtomicInteger failedStockOrders = new AtomicInteger(0);

        for (int i = 0; i < numThreads; i++) {
            User threadUser = new User("U" + i, "User " + i, "user" + i + "@example.com", "pass");
            shoppingService.registerUser(threadUser);
            
            executor.submit(() -> {
                ShoppingCart cart = new ShoppingCart();
                cart.addItem(product1, 1);
                
                try {
                    Order order = shoppingService.placeOrder(threadUser, cart, new CreditCardPayment());
                    if (order.getStatus() == OrderStatus.PROCESSING) {
                        successfulOrders.incrementAndGet();
                    }
                } catch (IllegalStateException e) {
                    failedStockOrders.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\n--- Flash Sale Results ---");
        System.out.println("Successful Orders: " + successfulOrders.get());
        System.out.println("Failed Orders (OOS): " + failedStockOrders.get());
        System.out.println("Remaining Smartphone Inventory: " + product1.getQuantity());
        
        if (successfulOrders.get() == 10 && product1.getQuantity() == 0) {
            System.out.println("SUCCESS: Atomic deduction prevented overselling.");
        } else {
            System.out.println("FAILED: Overselling detected or stock not zeroed correctly.");
        }
        
        System.out.println("\n--- Testing Saga Payment Failure ---");
        System.out.println("Initial Laptop Inventory: " + product2.getQuantity());
        ShoppingCart failCart = new ShoppingCart();
        failCart.addItem(product2, 5); // Attempt to buy 5 laptops
        
        try {
            // Using forceFailure = true
            shoppingService.placeOrder(user1, failCart, new CreditCardPayment(true));
        } catch (IllegalStateException e) {
            System.out.println("Caught Expected Exception: " + e.getMessage());
        }
        
        System.out.println("Laptop Inventory after Failed Saga: " + product2.getQuantity());
        if (product2.getQuantity() == 100) {
            System.out.println("SUCCESS: Compensating transaction completely reversed inventory hold.");
        } else {
            System.out.println("FAILED: Inventory was leaked during failed transaction.");
        }

        executor.shutdown();
    }

    public static void main(String[] args) {
        run();
    }
}
