package socialnetworkingservice_sde3.model.events;

import socialnetworkingservice_sde3.model.Post;
import java.util.UUID;

public class PostCreatedEvent implements Event {
    private final String eventId;
    private final long timestamp;
    private final Post post;

    public PostCreatedEvent(Post post) {
        this.eventId = UUID.randomUUID().toString();
        this.timestamp = System.currentTimeMillis();
        this.post = post;
    }

    @Override
    public String getEventId() { return eventId; }
    
    @Override
    public long getTimestamp() { return timestamp; }

    public Post getPost() { return post; }
}
