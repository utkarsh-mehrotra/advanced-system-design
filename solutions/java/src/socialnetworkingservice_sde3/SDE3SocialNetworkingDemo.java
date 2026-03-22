package socialnetworkingservice_sde3;

import socialnetworkingservice_sde3.database.GraphRepository;
import socialnetworkingservice_sde3.database.PostRepository;
import socialnetworkingservice_sde3.database.RedisCache;
import socialnetworkingservice_sde3.model.Post;
import socialnetworkingservice_sde3.service.NewsfeedService;
import socialnetworkingservice_sde3.service.PostService;
import socialnetworkingservice_sde3.service.SocialGraphService;
import socialnetworkingservice_sde3.worker.FanOutWorker;

import java.util.List;

public class SDE3SocialNetworkingDemo {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("🚀 Starting SDE3 Hybrid Push/Pull Social Networking Demo...");

        // 1. Initialize DBs
        GraphRepository graphDb = new GraphRepository();
        PostRepository postDb = new PostRepository();
        RedisCache redisCache = new RedisCache();

        // 2. Initialize Services
        SocialGraphService graphService = new SocialGraphService(graphDb);
        PostService postService = new PostService(postDb);
        NewsfeedService newsfeedService = new NewsfeedService(redisCache, postDb, graphService);
        
        // 3. Initialize Async Consumers
        FanOutWorker fanOutWorker = new FanOutWorker(graphService, redisCache);

        System.out.println("\n--- Setting up Social Graph ---");
        // A Celebrity with 1000 simulated followers (just triggering the code >= 500 celeb threshold)
        for (int i = 0; i < 500; i++) {
            graphService.follow("user_" + i, "JUSTIN_BIEBER");
        }

        // Normal friendship
        graphService.follow("ALICE", "BOB");
        graphService.follow("ALICE", "JUSTIN_BIEBER");

        System.out.println("Is Justin Bieber a celebrity? " + graphService.isCelebrity("JUSTIN_BIEBER"));
        System.out.println("Is Bob a celebrity? " + graphService.isCelebrity("BOB"));

        System.out.println("\n--- Triggering Posts ---");

        // Bob posts -> Should Fan-out to ALICE's Redis Cache
        postService.createPost("BOB", "Hey Alice, just had a great coffee! ☕");

        // Celebrity Posts -> Should NOT Fan-Out to 500 fake Redis caches. Saved only in PostDB.
        postService.createPost("JUSTIN_BIEBER", "Tuning my guitar for the next concert 🎸!");

        // Wait to allow Kafka (MessageBus) async worker to process Bob's post via thread
        System.out.println("Waiting for Kafka FanOutWorker to process the queues...");
        Thread.sleep(1000);

        System.out.println("\n--- Fetching ALICE's Newsfeed (Hybrid Pull) ---");
        List<Post> aliceFeed = newsfeedService.getTimeline("ALICE");
        
        aliceFeed.forEach(post -> System.out.println(" -> " + post));

        // Shutdown async threads
        fanOutWorker.shutdown();
        System.out.println("\nDemo Finished Successfully. Lock-Free and GC-Safe.");
        System.exit(0);
    }
}
