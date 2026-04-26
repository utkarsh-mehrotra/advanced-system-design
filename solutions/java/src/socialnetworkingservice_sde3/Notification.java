package socialnetworkingservice_sde3;

import java.sql.Timestamp;

public class Notification {
    private final String id;
    private final String userId;
    private final NotificationType type;
    private final String content;
    private final Timestamp timestamp;

    public Notification(String id, String userId, NotificationType type, String content, Timestamp timestamp) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.content = content;
        this.timestamp = timestamp;
    }

    public String getContent() { return content; }
}
