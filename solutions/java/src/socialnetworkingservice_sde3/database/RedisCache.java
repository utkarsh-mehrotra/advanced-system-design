package socialnetworkingservice_sde3.database;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * Simulates Redis Lists for Fan-Out-On-Write push model.
 */
public class RedisCache {
    // Key: userId, Value: List of materialized Post IDs in their timeline
    // Equivalent to LPUSH timeline:{userId} {postId} & LTRIM timeline:{userId} 0 800
    private final ConcurrentHashMap<String, CopyOnWriteArrayList<String>> userTimelines = new ConcurrentHashMap<>();
    private static final int MAX_FEED_LENGTH = 800; // Standard Redis optimization

    public void lPushTimeline(String userId, String postId) {
        userTimelines.compute(userId, (k, list) -> {
            if (list == null) list = new CopyOnWriteArrayList<>();
            list.add(0, postId); // Push to head
            // Simulated LTRIM
            if (list.size() > MAX_FEED_LENGTH) {
                list.remove(list.size() - 1);
            }
            return list;
        });
    }

    public List<String> lRangeTimeline(String userId, int limit) {
        CopyOnWriteArrayList<String> timeline = userTimelines.get(userId);
        if (timeline == null) return List.of();
        return timeline.stream().limit(limit).collect(Collectors.toList());
    }
}
