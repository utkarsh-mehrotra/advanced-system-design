package fooddeliveryservice_sde3;

import fooddeliveryservice_sde3.order.Order;
import fooddeliveryservice_sde3.order.OrderItem;
import fooddeliveryservice_sde3.order.OrderStatus;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FoodDeliveryServiceDemoUpgraded {
    public static void main(String[] args) throws InterruptedException {
        DeliveryMatchingStrategy matchingStrategy = new RandomOptimalAgentStrategy();
        FoodDeliveryFacade fas = new FoodDeliveryFacade(matchingStrategy);

        // Pre-compute basic entities
        Customer c1 = new Customer("C1", "John Doe", "john@gmail.com", "555-0101");
        Customer c2 = new Customer("C2", "Jane Smith", "jane@gmail.com", "555-0102");
        fas.registerCustomer(c1);
        fas.registerCustomer(c2);

        MenuItem pizza = new MenuItem("M1", "Pepperoni Pizza", "Italian", 15.99);
        Restaurant r1 = new Restaurant("R1", "Mario's Pizza", "123 Main St", List.of(pizza));
        fas.registerRestaurant(r1);

        // Register 1 single Delivery Driver!
        DeliveryAgent driverA = new DeliveryAgent("D1", "Mike (The Only Driver)", "555-0999");
        fas.registerDeliveryAgent(driverA);

        Order order1 = fas.placeOrder(c1.getId(), r1.getId(), List.of(new OrderItem(pizza, 1)));
        Order order2 = fas.placeOrder(c2.getId(), r1.getId(), List.of(new OrderItem(pizza, 2)));

        System.out.println("\\n--- SDE3 Concurrency Chaos Test ---");
        System.out.println("Simulating 2 simultaneous Backend systems CONFIRMING both orders at the precise same millisecond...");
        System.out.println("Because there is only 1 Driver, ONLY ONE order should successfully acquire Mike.");

        int threadCount = 2;
        ExecutorService chaosPool = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(1);

        chaosPool.submit(() -> {
            try {
                latch.await();
                fas.updateOrderStatus(order1.getId(), OrderStatus.CONFIRMED);
            } catch (Exception e) {}
        });
        chaosPool.submit(() -> {
            try {
                latch.await();
                fas.updateOrderStatus(order2.getId(), OrderStatus.CONFIRMED);
            } catch (Exception e) {}
        });

        // Fire both threads exactly simultaneously
        latch.countDown();

        Thread.sleep(1000);
        chaosPool.shutdown();
        
        System.out.println("\\nFinal Alignment:");
        System.out.println("Driver for Order 1: " + (order1.getDeliveryAgent() != null ? order1.getDeliveryAgent().getName() : "None"));
        System.out.println("Driver for Order 2: " + (order2.getDeliveryAgent() != null ? order2.getDeliveryAgent().getName() : "None"));

        Thread.sleep(500);
        fas.shutdownNetwork();
    }
}
