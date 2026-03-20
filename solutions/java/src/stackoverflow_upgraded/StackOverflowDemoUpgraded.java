package stackoverflow_upgraded;

import java.util.Arrays;
import java.util.List;

public class StackOverflowDemoUpgraded {
    public static void main(String[] args) {
        StackOverflowFacade api = new StackOverflowFacade();

        // 1. Create Users
        User alice = api.createUser("Alice", "alice@example.com");
        User bob = api.createUser("Bob", "bob@example.com");
        User charlie = api.createUser("Charlie", "charlie@example.com");

        // 2. Ask Question
        Question q1 = api.askQuestion(alice, "How to safely remove singletons?", "In Java, standard singletons...", Arrays.asList("java", "design-patterns"));
        System.out.println("Alice Asked: " + q1.getTitle());

        // 3. Thread-Safe Answering
        Answer a1 = api.answerQuestion(bob, q1, "Use constructor DI and simple POJOs!");
        System.out.println("Bob Answered: " + a1.getContent());

        // 4. Thread-Safe Voting & Event Driven Math
        System.out.println("\n--- Event Driven Reputation Engine Active ---");
        System.out.println("Alice Reputation Before Vote: " + alice.getReputation());
        
        // Charlie Upvotes Alice's Question
        api.upvoteQuestion(charlie, q1);
        System.out.println("Charlie upvoted Alice's question.");
        
        System.out.println("Alice Reputation After Question Upvote (+5) : " + alice.getReputation());

        // 5. Accepting Answers
        System.out.println("\nBob Reputation Before Acceptance: " + bob.getReputation());
        api.acceptAnswer(a1);
        System.out.println("Alice accepted Bob's Answer.");
        System.out.println("Bob Reputation After Answer Accepted (+15): " + bob.getReputation());

        // 6. Searching
        System.out.println("\n--- Fast Snapshot Array Search ---");
        List<Question> results = api.searchQuestions("design-patterns");
        System.out.println("Found " + results.size() + " question(s) matching 'design-patterns'.");
        System.out.println(results.get(0).getTitle());
    }
}
