package linkedin_sde3;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

public class LinkedInManager {
    // Thread safe domain stores
    private final Map<String, User> users = new ConcurrentHashMap<>();
    private final Map<String, Set<String>> connections = new ConcurrentHashMap<>();
    private final List<FeedItem> globalPosts = new CopyOnWriteArrayList<>();
    
    // Strategy
    private FeedStrategy defaultStrategy = new ChronologicalFeedStrategy();

    public void registerUser(User u) {
        users.put(u.getUserId(), u);
        connections.put(u.getUserId(), new CopyOnWriteArraySet<>());
    }

    public void addConnection(String u1, String u2) {
        Set<String> c1 = connections.get(u1);
        Set<String> c2 = connections.get(u2);
        if (c1 != null && c2 != null) {
            c1.add(u2);
            c2.add(u1);
        }
    }

    public void createPost(FeedItem item) {
        globalPosts.add(item);
    }

    public List<FeedItem> generateFeed(String userId) {
        Set<String> userConnections = connections.get(userId);
        if (userConnections == null) return List.of();

        List<FeedItem> result = new CopyOnWriteArrayList<>();
        for (FeedItem post : globalPosts) {
            if (userConnections.contains(post.getAuthorId())) {
                result.add(post);
            }
        }
        
        // Apply sorting strategy
        defaultStrategy.sortFeed(result);
        return result;
    }
}
