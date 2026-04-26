package stackoverflow_sde3;

import java.time.LocalDateTime;

public class Comment {
    private final int id;
    private final String content;
    private final User author;
    private final LocalDateTime creationDate;

    public Comment(int id, User author, String content) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.creationDate = LocalDateTime.now();
    }

    public String getContent() { return content; }
    public User getAuthor() { return author; }
    public LocalDateTime getCreationDate() { return creationDate; }
}
