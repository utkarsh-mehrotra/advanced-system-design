package stackoverflow_sde3;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class StackOverflowDemoSDE3 {
    public static void main(String[] args) throws InterruptedException {
        new ReputationService(); 

        PlatformEngine engine = new PlatformEngine();
        engine.postQuestion(new Question("Q_99", "AUTHOR_X"));
        
        System.out.println("Simulating viral thread...");
        ExecutorService executor = Executors.newFixedThreadPool(5);
        for(int i = 0; i < 10; i++) {
            executor.submit(() -> engine.castVote("Q_99"));
        }

        executor.shutdown();
        executor.awaitTermination(2, TimeUnit.SECONDS);
        
        System.out.println("Final Viral Count: ");
        engine.viewTrending();
    }
}
