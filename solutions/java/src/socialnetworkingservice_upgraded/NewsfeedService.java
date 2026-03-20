package socialnetworkingservice_upgraded;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Executes the SDE3 FAANG design pattern: Fan-Out-On-Write (Push Model).
 * Pre-computes timelines for users asynchronously.
 */
public class NewsfeedService {
    private final ExecutorService fanOutPool;
    private final Map<String, User> userRepository;

    public NewsfeedService(Map<String, User> userRepository) {
        this.userRepository = userRepository;
        // Background thread pool exclusively solving timeline generation
        this.fanOutPool = Executors.newFixedThreadPool(10);
    }

    /**
     * SDE3: Pushes the Post ID to the pre-computed Newsfeed Array of all friends.
     * This makes getNewsfeed O(1) read time!
     */
    public void pushPostToFollowersTimeline(String authorId, String postId) {
        fanOutPool.submit(() -> {
            User author = userRepository.get(authorId);
            if (author != null) {
                // Fan-out to self
                author.getNewsfeedPostIds().add(0, postId); // Add to head of timeline
                
                // Fan-out to all friends
                for (String friendId : author.getFriendIds()) {
                    User friend = userRepository.get(friendId);
                    if (friend != null) {
                        try {
                            // Simulate processing delay for thousands of friends
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                        
                        friend.getNewsfeedPostIds().add(0, postId);
                        System.out.println("[Fan-Out Pipeline]: Pushed Post " + postId + " to " + friend.getName() + "'s Newsfeed Cache.");
                    }
                }
            }
        });
    }

    public void shutdownTaskPool() {
        fanOutPool.shutdown();
    }
}
