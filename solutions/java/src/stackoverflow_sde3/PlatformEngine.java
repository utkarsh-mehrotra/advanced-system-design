package stackoverflow_sde3;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class PlatformEngine {
    private final Map<String, Question> questions = new ConcurrentHashMap<>();
    private final ReentrantReadWriteLock cacheLock = new ReentrantReadWriteLock();

    public void postQuestion(Question question) {
        cacheLock.writeLock().lock();
        try {
            questions.put(question.getQuestionId(), question);
        } finally {
            cacheLock.writeLock().unlock();
        }
    }

    public void viewTrending() {
        cacheLock.readLock().lock(); // Read block allowing thousands of concurrent web scrapers
        try {
            questions.values().forEach(q -> 
                System.out.println("Q: " + q.getQuestionId() + " | Votes: " + q.getUpvotes())
            );
        } finally {
            cacheLock.readLock().unlock();
        }
    }

    public void castVote(String questionId) {
        Question q = questions.get(questionId);
        if (q != null) q.applyUpvote();
    }
}
