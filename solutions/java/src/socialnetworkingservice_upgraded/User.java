package socialnetworkingservice_upgraded;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class User {
    private final String id;
    private final String name;
    
    // SDE3: Viral scaling array lists.
    private final List<String> friendIds;
    private final List<String> postIds;
    
    // SDE3: The Fan-Out Feed Cache! 
    // This allows O(1) Timeline reading rather than dynamically querying thousands of friend's graphs.
    private final List<String> newsfeedPostIds;

    public User(String id, String name) {
        this.id = id;
        this.name = name;
        this.friendIds = new CopyOnWriteArrayList<>();
        this.postIds = new CopyOnWriteArrayList<>();
        this.newsfeedPostIds = new CopyOnWriteArrayList<>();
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public List<String> getFriendIds() { return friendIds; }
    public List<String> getPostIds() { return postIds; }
    public List<String> getNewsfeedPostIds() { return newsfeedPostIds; }
}
