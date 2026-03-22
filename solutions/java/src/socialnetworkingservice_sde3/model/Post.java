package socialnetworkingservice_sde3.model;

import java.util.UUID;

public class Post {
    private final String id;
    private final String authorId;
    private final String content;
    private final long timestamp;

    public Post(String authorId, String content) {
        this.id = UUID.randomUUID().toString();
        this.authorId = authorId;
        this.content = content;
        this.timestamp = System.currentTimeMillis();
    }

    public String getId() { return id; }
    public String getAuthorId() { return authorId; }
    public String getContent() { return content; }
    public long getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return "Post[" + id.substring(0,8) + " by " + authorId + ": " + content + "]";
    }
}
