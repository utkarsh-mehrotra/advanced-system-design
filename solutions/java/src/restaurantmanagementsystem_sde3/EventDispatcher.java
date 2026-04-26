package restaurantmanagementsystem_sde3;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * SDE3: The Asynchronous Event Dispatcher completely decoupling
 * the POS (Point Of Sale) terminal threads from the Kitchen Displays.
 */
public class EventDispatcher {
    // Background pool ensuring the main customer threads NEVER wait for standard network push notifications
    private final ExecutorService executorService;

    public EventDispatcher() {
        // High core count to rapidly scatter events purely in the background
        this.executorService = Executors.newFixedThreadPool(10);
    }

    public void dispatchKitchenNotification(Order order) {
        executorService.submit(() -> {
            try {
                // Simulate network latency to the Kitchen iPad
                Thread.sleep(500);
                System.out.println("[BACKGROUND EVENT]: Kitchen Display Updated - Start preparing Order #" + order.getId() + " - " + order.getItems().size() + " items.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }

    public void dispatchStaffNotification(Order order) {
        executorService.submit(() -> {
            try {
                Thread.sleep(200);
                System.out.println("[BACKGROUND EVENT]: Waiter Pager Alert - Order #" + order.getId() + " is now " + order.getStatus());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }
    
    public void shutdown() {
        executorService.shutdown();
    }
}
