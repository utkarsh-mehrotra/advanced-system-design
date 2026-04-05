package linkedin_sde3;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LinkedInManager {
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();

    public void addPost(String userId, String content) {
        rwLock.writeLock().lock();
        try {
            // Internal logic...
            System.out.println("LinkedInManager: Post persisted physically for " + userId);
            
            // Emit Event asynchronously
            EventBus.getInstance().publish("USER_POST_CREATED", 
                new ActivityLog(userId, "NEW_POST: " + content, "TS=NOW"));
        } finally {
            rwLock.writeLock().unlock();
        }
    }
}
