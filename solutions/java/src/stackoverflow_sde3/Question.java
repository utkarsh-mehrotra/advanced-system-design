package stackoverflow_sde3;

import java.util.concurrent.atomic.AtomicInteger;

public class Question {
    private final String questionId;
    private final String authorId;
    private final AtomicInteger upvoteCount;

    public Question(String questionId, String authorId) {
        this.questionId = questionId;
        this.authorId = authorId;
        this.upvoteCount = new AtomicInteger(0);
    }

    public String getQuestionId() { return questionId; }
    public String getAuthorId() { return authorId; }
    public int getUpvotes() { return upvoteCount.get(); }

    public void applyUpvote() {
        upvoteCount.incrementAndGet(); // CPU level primitive atomic increment
        EventBus.getInstance().publish("UPVOTE_RECEIVED", authorId);
    }
}
