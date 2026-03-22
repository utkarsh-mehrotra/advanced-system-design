package socialnetworkingservice_sde3.service;

import socialnetworkingservice_sde3.database.GraphRepository;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Manages the social graph and holds the heuristic for "Celebrity" vs "Normal" user.
 * Celebrities use Fan-out-on-read (Pull). Normal users use Fan-out-on-write (Push).
 */
public class SocialGraphService {
    private final GraphRepository graphDb;
    private static final int CELEBRITY_THRESHOLD = 500; // In reality: 10,000+

    public SocialGraphService(GraphRepository graphDb) {
        this.graphDb = graphDb;
    }

    public void follow(String followerId, String followeeId) {
        graphDb.follow(followerId, followeeId);
        System.out.println("[GRAPH] " + followerId + " followed " + followeeId);
    }

    public boolean isCelebrity(String userId) {
        return graphDb.getFollowers(userId).size() >= CELEBRITY_THRESHOLD;
    }

    public Set<String> getFollowers(String userId) {
        return graphDb.getFollowers(userId);
    }

    /** Retrieves all celebrities that a specific user is currently following */
    public Set<String> getCelebritiesFollowedBy(String userId) {
        return graphDb.getFollowing(userId).stream()
            .filter(this::isCelebrity)
            .collect(Collectors.toSet());
    }
}
