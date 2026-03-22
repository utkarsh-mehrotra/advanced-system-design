package socialnetworkingservice_sde3.worker;

import socialnetworkingservice_sde3.database.RedisCache;
import socialnetworkingservice_sde3.messaging.MessageBus;
import socialnetworkingservice_sde3.model.events.PostCreatedEvent;
import socialnetworkingservice_sde3.service.SocialGraphService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Background consumer processing `posts.created` Kafka topic.
 * Fans out the Post ID to followers' Redis timelines ONLY IF the author is NOT a celebrity.
 */
public class FanOutWorker {
    private final SocialGraphService graphService;
    private final RedisCache redisCache;
    private final ExecutorService workerThread = Executors.newSingleThreadExecutor();

    public FanOutWorker(SocialGraphService graphService, RedisCache redisCache) {
        this.graphService = graphService;
        this.redisCache = redisCache;
        start();
    }

    private void start() {
        workerThread.submit(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    PostCreatedEvent event = MessageBus.getInstance().subscribeToPostCreatedEvents().take();
                    processFanOut(event);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }

    private void processFanOut(PostCreatedEvent event) {
        String authorId = event.getPost().getAuthorId();
        String postId = event.getPost().getId();

        if (graphService.isCelebrity(authorId)) {
            // HYBRID PULL: Do NOT push to 10M followers' timelines. 
            // The NewsfeedService will merge celebrity posts on-read.
            System.out.println("[FANOUT WORKER] Ignoring Fan-Out for Celebrity: " + authorId);
            return;
        }

        // HYBRID PUSH: Iterate over followers and push to Redis (O(N))
        // Since it's a normal user, N should be small (< 10_000).
        int pushedCount = 0;
        for (String followerId : graphService.getFollowers(authorId)) {
            redisCache.lPushTimeline(followerId, postId);
            pushedCount++;
        }
        
        System.out.println("[FANOUT WORKER] Fanned out post " + postId.substring(0,8) + " to " + pushedCount + " timelines.");
    }
    
    public void shutdown() {
        workerThread.shutdownNow();
    }
}
