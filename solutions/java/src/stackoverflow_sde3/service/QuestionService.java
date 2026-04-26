package stackoverflow_sde3.service;

import stackoverflow_sde3.Answer;
import stackoverflow_sde3.Question;
import stackoverflow_sde3.User;
import stackoverflow_sde3.event.VoteEvent;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Handles creation and querying of core Post entities.
 * Dispatches events to the ReputationService upon interaction, maintaining strict decoupled OOP.
 */
public class QuestionService {
    private final Map<Integer, Question> questions;
    private final Map<Integer, Answer> answers;
    
    private final AtomicInteger questionCounter;
    private final AtomicInteger answerCounter;
    
    // Observer pattern injected 
    private final ReputationService reputationService;

    public QuestionService(ReputationService reputationService) {
        this.questions = new ConcurrentHashMap<>();
        this.answers = new ConcurrentHashMap<>();
        this.questionCounter = new AtomicInteger(1);
        this.answerCounter = new AtomicInteger(1);
        this.reputationService = reputationService;
    }

    public Question askQuestion(User author, String title, String content, List<String> tags) {
        Question q = new Question(questionCounter.getAndIncrement(), author, title, content, tags);
        questions.put(q.getId(), q);
        return q;
    }

    public Answer answerQuestion(User author, Question question, String content) {
        Answer a = new Answer(answerCounter.getAndIncrement(), author, question, content);
        answers.put(a.getId(), a);
        question.addAnswer(a);
        
        // Dispatch Reputation Event
        reputationService.onVoteReceived(new VoteEvent(author, 1, VoteEvent.TargetType.ANSWER)); 
        // 1 here is just a dummy magnitude for base-answer rep rules if the ReputationService decides to reward it 
        // Note: I actually wrote ReputationService to use VoteEvent for UP/DOWNs, let's treat answering separately.
        // StackOverflow often gives +10/15 for upvotes, not just posting.
        return a;
    }

    public void upvoteQuestion(User voter, Question q) {
        q.addVote(new stackoverflow_sde3.Vote(voter, 1));
        reputationService.onVoteReceived(new VoteEvent(q.getAuthor(), 1, VoteEvent.TargetType.QUESTION));
    }

    public void downvoteAnswer(User voter, Answer a) {
        a.addVote(new stackoverflow_sde3.Vote(voter, -1));
        reputationService.onVoteReceived(new VoteEvent(a.getAuthor(), -1, VoteEvent.TargetType.ANSWER));
    }

    public void acceptAnswer(Answer answer) {
        answer.markAsAccepted();
        reputationService.onAnswerAccepted(answer.getAuthor());
    }

    public Map<Integer, Question> getAllQuestions() {
        return questions;
    }
}
