package socialnetworkingservice_sde3.service;

import socialnetworkingservice_sde3.database.PostRepository;
import socialnetworkingservice_sde3.database.RedisCache;
import socialnetworkingservice_sde3.model.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Generates the User's timeline using Hybrid Pull.
 */
public class NewsfeedService {
    private final RedisCache redisCache;
    private final PostRepository postDb;
    private final SocialGraphService graphService;
    
    private static final int TIMELINE_LIMIT = 20;

    public NewsfeedService(RedisCache redisCache, PostRepository postDb, SocialGraphService graphService) {
        this.redisCache = redisCache;
        this.postDb = postDb;
        this.graphService = graphService;
    }

    /**
     * SDE3 Hybrid Pull Timeline Generation:
     * 1. O(1) Fetch pre-computed material list from Redis (Normal users' posts).
     * 2. O(K) Fetch recent posts from explicitly followed Celebrities directly from Cassandra.
     * 3. Merge, sort by timestamp DESC, take Top 20.
     */
    public List<Post> getTimeline(String userId) {
        // Step 1: Read pre-populated Redis Timeline (Normal users Pushed here)
        List<String> cachedPostIds = redisCache.lRangeTimeline(userId, TIMELINE_LIMIT);
        List<Post> timeline = new ArrayList<>();
        
        for (String postId : cachedPostIds) {
            Post post = postDb.getPost(postId);
            if (post != null) {
                timeline.add(post);
            }
        }

        // Step 2: Merge Celebrity Tweets dynamically (Pull Model)
        Set<String> followedCelebrities = graphService.getCelebritiesFollowedBy(userId);
        for (String celebId : followedCelebrities) {
            // For each celebrity, pull their recent tweets from PostDB straight away
            List<Post> celebPosts = postDb.getRecentPostsByAuthor(celebId, TIMELINE_LIMIT);
            timeline.addAll(celebPosts);
        }

        // Step 3: Global Sort & Truncate
        return timeline.stream()
            .sorted((p1, p2) -> Long.compare(p2.getTimestamp(), p1.getTimestamp())) // DESC
            .limit(TIMELINE_LIMIT)
            .collect(Collectors.toList());
    }
}
