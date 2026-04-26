package linkedin_sde3;

import java.util.Date;

public class FeedItem {
    private final String id;
    private final String authorId;
    private final String content;
    private final long timestamp;

    public FeedItem(String id, String authorId, String content) {
        this.id = id;
        this.authorId = authorId;
        this.content = content;
        this.timestamp = System.currentTimeMillis();
    }

    public String getAuthorId() { return authorId; }
    public String getContent() { return content; }
    public long getTimestamp() { return timestamp; }
}
