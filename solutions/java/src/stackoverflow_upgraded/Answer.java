package stackoverflow_upgraded;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class Answer implements Votable, Commentable {
    private final int id;
    private final String content;
    private final User author;
    private final Question question;
    private final AtomicBoolean isAccepted;
    private final LocalDateTime creationDate;

    // SDE3: Thread-Safe Collections
    private final List<Comment> comments;
    private final List<Vote> votes;

    public Answer(int id, User author, Question question, String content) {
        this.id = id;
        this.author = author;
        this.question = question;
        this.content = content;
        this.creationDate = LocalDateTime.now();
        this.isAccepted = new AtomicBoolean(false);

        this.comments = new CopyOnWriteArrayList<>();
        this.votes = new CopyOnWriteArrayList<>();
    }

    public void markAsAccepted() {
        isAccepted.set(true);
    }

    public boolean isAccepted() {
        return isAccepted.get();
    }

    @Override
    public void addVote(Vote vote) {
        votes.removeIf(v -> v.getUser().equals(vote.getUser()));
        votes.add(vote);
    }

    @Override
    public int getVoteCount() {
        return votes.stream().mapToInt(Vote::getValue).sum();
    }

    @Override
    public void addComment(Comment comment) {
        comments.add(comment);
    }

    @Override
    public List<Comment> getComments() {
        return Collections.unmodifiableList(comments);
    }

    public int getId() { return id; }
    public String getContent() { return content; }
    public User getAuthor() { return author; }
    public Question getQuestion() { return question; }
    public LocalDateTime getCreationDate() { return creationDate; }
}
