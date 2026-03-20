package stackoverflow_upgraded;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class Question implements Votable, Commentable {
    private final int id;
    private final String title;
    private final String content;
    private final User author;
    private final LocalDateTime creationDate;
    
    // SDE3: Thread-Safe Collections!
    private final List<Answer> answers;
    private final List<Comment> comments;
    private final List<Tag> tags;
    private final List<Vote> votes;

    public Question(int id, User author, String title, String content, List<String> tagNames) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.content = content;
        this.creationDate = LocalDateTime.now();
        
        // Allows thousands of users to independently vote/answer without throwing ConcurrentModificationExceptions
        this.answers = new CopyOnWriteArrayList<>();
        this.comments = new CopyOnWriteArrayList<>();
        this.votes = new CopyOnWriteArrayList<>();
        
        this.tags = tagNames.stream().map(Tag::new).collect(Collectors.toList());
    }

    public void addAnswer(Answer answer) {
        // CopyOnWriteArrayList handles internal synchronization
        if (!answers.contains(answer)) {
            answers.add(answer);
        }
    }

    @Override
    public void addVote(Vote vote) {
        // Safely remove any previous vote by the same user to prevent spam double-voting
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
    public User getAuthor() { return author; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public LocalDateTime getCreationDate() { return creationDate; }
    public List<Answer> getAnswers() { return Collections.unmodifiableList(answers); }
    public List<Tag> getTags() { return Collections.unmodifiableList(tags); }
}
