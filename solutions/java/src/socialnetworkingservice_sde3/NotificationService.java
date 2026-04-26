package socialnetworkingservice_sde3;

import java.sql.Timestamp;
import java.util.Map;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NotificationService {
    private final Map<String, List<Notification>> notificationStore;
    private final ExecutorService dispatchPool;

    public NotificationService() {
        this.notificationStore = new ConcurrentHashMap<>();
        this.dispatchPool = Executors.newFixedThreadPool(5);
    }

    public void dispatchNotification(String targetUserId, NotificationType type, String message) {
        dispatchPool.submit(() -> {
            try {
                // Simulate network Apple Push Notification Ping
                Thread.sleep(100); 
                Notification notif = new Notification(
                    UUID.randomUUID().toString(), 
                    targetUserId, 
                    type, 
                    message, 
                    new Timestamp(System.currentTimeMillis())
                );
                
                // Ensure target has a thread-safe list to receive alerts
                notificationStore.computeIfAbsent(targetUserId, k -> new CopyOnWriteArrayList<>()).add(notif);
                
                System.out.println("[Background Push Notification -> " + targetUserId + "]: " + message);
            } catch (InterruptedException e) {
                 Thread.currentThread().interrupt();
            }
        });
    }

    public List<Notification> getNotificationsForUser(String userId) {
        return notificationStore.getOrDefault(userId, new CopyOnWriteArrayList<>());
    }

    public void shutdownTaskPool() {
        dispatchPool.shutdown();
    }
}
