package socialnetworkingservice_sde3.database;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Simulates a Graph Database (like Neo4j or pre-computed Redis sets) for followers.
 */
public class GraphRepository {
    // Adjacency lists
    private final ConcurrentHashMap<String, CopyOnWriteArraySet<String>> followers = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, CopyOnWriteArraySet<String>> following = new ConcurrentHashMap<>();

    public void follow(String followerId, String followeeId) {
        following.computeIfAbsent(followerId, k -> new CopyOnWriteArraySet<>()).add(followeeId);
        followers.computeIfAbsent(followeeId, k -> new CopyOnWriteArraySet<>()).add(followerId);
    }

    public Set<String> getFollowers(String userId) {
        return followers.getOrDefault(userId, new CopyOnWriteArraySet<>());
    }

    public Set<String> getFollowing(String userId) {
        return following.getOrDefault(userId, new CopyOnWriteArraySet<>());
    }
}
