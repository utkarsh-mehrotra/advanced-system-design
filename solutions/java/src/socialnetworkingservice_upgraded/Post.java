package socialnetworkingservice_upgraded;

import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Post {
    private final String id;
    private final String userId;
    private final String content;
    private final Timestamp timestamp;
    
    // SDE3: Neutralize ConcurrentModificationExceptions when 10,000 users like a viral post instantly.
    // Replaced standard ArrayList.
    private final List<String> likes;
    private final List<Comment> comments;

    public Post(String id, String userId, String content, Timestamp timestamp) {
        this.id = id;
        this.userId = userId;
        this.content = content;
        this.timestamp = timestamp;
        this.likes = new CopyOnWriteArrayList<>();
        this.comments = new CopyOnWriteArrayList<>();
    }

    public String getId() { return id; }
    public String getUserId() { return userId; }
    public String getContent() { return content; }
    public Timestamp getTimestamp() { return timestamp; }
    public List<String> getLikes() { return likes; }
    public List<Comment> getComments() { return comments; }
}
