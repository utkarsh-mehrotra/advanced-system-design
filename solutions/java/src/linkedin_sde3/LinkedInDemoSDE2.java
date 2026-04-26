package linkedin_sde3;

import java.util.List;

public class LinkedInDemoSDE2 {
    public static void main(String[] args) {
        LinkedInManager manager = new LinkedInManager();
        
        manager.registerUser(new User("u1", "Alice"));
        manager.registerUser(new User("u2", "Bob"));
        manager.registerUser(new User("u3", "Charlie"));

        manager.addConnection("u1", "u2");

        manager.createPost(new FeedItem("post1", "u2", "Hello LinkedIn from Bob!"));
        manager.createPost(new FeedItem("post2", "u3", "Charlie's invisible post to Alice."));
        
        List<FeedItem> aliceFeed = manager.generateFeed("u1");
        System.out.println("Alice's Feed size: " + aliceFeed.size());
        if (!aliceFeed.isEmpty()) {
            System.out.println("Top post: " + aliceFeed.get(0).getContent());
        }
    }
}
