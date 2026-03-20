package fooddeliveryservice_upgraded;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * SDE3 Notification Dispatcher.
 * Uses a Thread Pool to fire remote events (like SMS / Push) absolutely transparently.
 * The core order loop no longer halts waiting for users to 'receive' their alerts.
 */
public class NotificationDispatcher {
    private final ExecutorService executorService;

    public NotificationDispatcher() {
        this.executorService = Executors.newFixedThreadPool(10);
    }

    public void dispatchCustomerNotification(String message) {
        executorService.submit(() -> {
            try {
                // Simulate slow external network ping
                Thread.sleep(150);
                System.out.println("[Push Notification -> Customer]: " + message);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }

    public void dispatchDriverNotification(String driverName, String message) {
        executorService.submit(() -> {
            try {
                Thread.sleep(100);
                System.out.println("[SMS Notification -> Driver: " + driverName + "]: " + message);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }

    public void dispatchRestaurantNotification(String restaurantName, String message) {
        executorService.submit(() -> {
            try {
                Thread.sleep(100);
                System.out.println("[Tablet Notification -> Restaurant: " + restaurantName + "]: " + message);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }
    
    public void shutdownTaskPool() {
        executorService.shutdown();
    }
}
