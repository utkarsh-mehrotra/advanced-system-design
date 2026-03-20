package socialnetworkingservice_upgraded;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Social Networking Facade encapsulating the microservices.
 */
public class SocialNetworkingFacade {
    private final Map<String, User> userRepository;
    private final Map<String, Post> postRepository;
    
    private final NewsfeedService newsfeedService;
    private final NotificationService notificationService;

    public SocialNetworkingFacade() {
        this.userRepository = new ConcurrentHashMap<>();
        this.postRepository = new ConcurrentHashMap<>();
        
        this.newsfeedService = new NewsfeedService(userRepository);
        this.notificationService = new NotificationService();
    }

    public void registerUser(User user) {
        userRepository.put(user.getId(), user);
    }

    public void addFriend(String userIdA, String userIdB) {
        User userA = userRepository.get(userIdA);
        User userB = userRepository.get(userIdB);
        if (userA != null && userB != null) {
            userA.getFriendIds().add(userIdB);
            userB.getFriendIds().add(userIdA);
            notificationService.dispatchNotification(userIdB, NotificationType.FRIEND_REQUEST_ACCEPTED, userA.getName() + " became your friend.");
        }
    }

    public Post createPost(String userId, String content) {
        User user = userRepository.get(userId);
        if (user != null) {
            Post post = new Post(UUID.randomUUID().toString(), userId, content, new Timestamp(System.currentTimeMillis()));
            postRepository.put(post.getId(), post);
            user.getPostIds().add(post.getId());

            // SDE3 Core Execution: Trigger Fan-Out Background Pipeline
            newsfeedService.pushPostToFollowersTimeline(userId, post.getId());
            
            return post;
        }
        return null;
    }

    /**
     * SDE3: O(1) Pre-Computed Newsfeed Access! 
     * Gone is the catastrophic loops and O(N) memory allocations!
     */
    public List<Post> getNewsfeed(String userId) {
        List<Post> feed = new ArrayList<>();
        User user = userRepository.get(userId);
        if (user != null) {
            // Instantly grab pre-computed feed topology
            for (String postId : user.getNewsfeedPostIds()) {
                Post post = postRepository.get(postId);
                if (post != null) {
                    feed.add(post);
                }
            }
        }
        return feed; // Already strictly ordered by Fan-Out Array Head Push
    }

    public void likePost(String userId, String postId) {
        Post post = postRepository.get(postId);
        // CopyOnWriteArrayList absorbs concurrency naturally! No ConcurrentModificationExceptions
        if (post != null && !post.getLikes().contains(userId)) {
            post.getLikes().add(userId);
            
            User liker = userRepository.get(userId);
            notificationService.dispatchNotification(post.getUserId(), NotificationType.LIKE, liker.getName() + " liked your post.");
        }
    }

    public void shutdownBackends() {
        newsfeedService.shutdownTaskPool();
        notificationService.shutdownTaskPool();
    }
}
