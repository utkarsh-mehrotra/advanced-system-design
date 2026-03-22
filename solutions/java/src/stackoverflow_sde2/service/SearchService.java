package stackoverflow_sde2.service;

import stackoverflow_sde2.Question;

import java.util.List;
import java.util.stream.Collectors;

public class SearchService {
    private final QuestionService questionService;

    public SearchService(QuestionService questionService) {
        this.questionService = questionService;
    }

    public List<Question> searchQuestions(String query) {
        final String searchLower = query.toLowerCase();
        
        // Converts from the Concurrent Map snapshot efficiently
        return questionService.getAllQuestions().values().stream()
                .filter(q -> q.getTitle().toLowerCase().contains(searchLower) ||
                             q.getContent().toLowerCase().contains(searchLower) ||
                             q.getTags().stream().anyMatch(t -> t.getName().equalsIgnoreCase(searchLower)))
                .collect(Collectors.toList());
    }
}
