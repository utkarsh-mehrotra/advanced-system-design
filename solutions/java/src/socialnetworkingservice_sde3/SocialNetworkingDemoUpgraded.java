package socialnetworkingservice_sde3;

import java.util.List;

public class SocialNetworkingDemoUpgraded {
    public static void main(String[] args) throws InterruptedException {
        SocialNetworkingFacade sns = new SocialNetworkingFacade();

        // 1. Seed Network
        User u1 = new User("U1", "Alice");
        User u2 = new User("U2", "Bob");
        User u3 = new User("U3", "Charlie");
        sns.registerUser(u1);
        sns.registerUser(u2);
        sns.registerUser(u3);

        sns.addFriend("U1", "U2");
        sns.addFriend("U1", "U3");

        System.out.println("\\n--- SDE3 Fan-Out-On-Write Action Test ---");
        System.out.println("Alice creates a Post. This drops the network request immediately, whilst worker threads fan out the feed in the background...");
        Post postObject = sns.createPost("U1", "I just got promoted to L6!");
        System.out.println("Main Thread: Post Created (" + postObject.getId() + "). Waiting to simulate background processes...\n");

        // Let the ExecutorService catch up and Fan-out the Post
        Thread.sleep(100);

        System.out.println("\\n--- O(1) Feed Pulls ---");
        List<Post> bobsFeed = sns.getNewsfeed("U2");
        List<Post> charliesFeed = sns.getNewsfeed("U3");
        
        System.out.println("Bob's Pre-Computed Feed Length: " + bobsFeed.size() + " (" + (bobsFeed.isEmpty() ? "Empty" : bobsFeed.get(0).getContent()) + ")");
        System.out.println("Charlie's Pre-Computed Feed Length: " + charliesFeed.size() + " (" + (charliesFeed.isEmpty() ? "Empty" : charliesFeed.get(0).getContent()) + ")");

        System.out.println("\\n--- Thread-Safe Like Swarms ---");
        // Simulated Concurrent viral likes
        sns.likePost("U2", postObject.getId());
        sns.likePost("U3", postObject.getId());

        Thread.sleep(200);
        
        System.out.println("Total Likes on Post (CopyOnWriteArrayList): " + postObject.getLikes().size());

        sns.shutdownBackends();
    }
}
