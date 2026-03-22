package stackoverflow_sde2;

import stackoverflow_sde2.service.QuestionService;
import stackoverflow_sde2.service.ReputationService;
import stackoverflow_sde2.service.SearchService;
import stackoverflow_sde2.service.UserService;

import java.util.List;

/**
 * Acts as the orchestration facade for clients (e.g., API controllers)
 */
public class StackOverflowFacade {
    private final UserService userService;
    private final QuestionService questionService;
    private final SearchService searchService;
    private final ReputationService reputationService;

    public StackOverflowFacade() {
        this.reputationService = new ReputationService();
        this.userService = new UserService();
        // inject dependencies explicitly
        this.questionService = new QuestionService(reputationService);
        this.searchService = new SearchService(questionService);
    }

    public User createUser(String username, String email) {
        return userService.createUser(username, email);
    }

    public Question askQuestion(User user, String title, String content, List<String> tags) {
        return questionService.askQuestion(user, title, content, tags);
    }

    public Answer answerQuestion(User user, Question question, String content) {
        return questionService.answerQuestion(user, question, content);
    }

    public void upvoteQuestion(User voter, Question q) {
        questionService.upvoteQuestion(voter, q);
    }

    public void addComment(User user, Commentable target, String text) {
        target.addComment(new Comment(0, user, text));
    }

    public void acceptAnswer(Answer answer) {
        questionService.acceptAnswer(answer);
    }

    public List<Question> searchQuestions(String query) {
        return searchService.searchQuestions(query);
    }
}
