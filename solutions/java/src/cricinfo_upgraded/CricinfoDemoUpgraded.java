package cricinfo_upgraded;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CricinfoDemoUpgraded {
    public static void run() {
        MatchService matchService = new MatchServiceImpl();
        ScorecardService scorecardService = new ScorecardServiceImpl();
        CricinfoFacade cricinfo = new CricinfoFacade(matchService, scorecardService);

        // Create players
        Player p1 = new Player("P1", "Virat Kohli", "Batsman");
        Player p2 = new Player("P2", "Jasprit Bumrah", "Bowler");
        Player p3 = new Player("P3", "Steve Smith", "Batsman");
        Player p4 = new Player("P4", "Pat Cummins", "Bowler");

        Team india = new Team("T1", "India", List.of(p1, p2));
        Team australia = new Team("T2", "Australia", List.of(p3, p4));

        Match match = new Match("M1", "India vs Australia", "MCG", LocalDateTime.now(), List.of(india, australia));
        cricinfo.addMatch(match);
        cricinfo.updateMatchStatus("M1", MatchStatus.IN_PROGRESS);

        cricinfo.createScorecard(match);
        String scorecardId = "SC-M1-0001";

        System.out.println("Match: " + match.getTitle() + " | Status: " + match.getStatus());

        Innings firstInnings = new Innings("I1", "T1", "T2");
        cricinfo.recordInnings(scorecardId, firstInnings);

        // Simulate concurrent ball-by-ball updates from multiple fast-reporting sensor threads
        // We will simulate 120 balls (20 overs) bowled concurrently, each adding exactly 1 run for simplicity
        int totalBalls = 120;
        int threads = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        CountDownLatch latch = new CountDownLatch(totalBalls);

        for (int i = 0; i < totalBalls; i++) {
            executor.submit(() -> {
                cricinfo.addRunsToScore(scorecardId, "T1", 1);
                latch.countDown();
            });
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        executor.shutdown();

        // Print final score
        Scorecard sc = cricinfo.getScorecard(scorecardId);
        int finalScore = sc.getTeamScores().get("T1").get();
        System.out.println("Expected Score for India: " + totalBalls);
        System.out.println("Actual Score computed via AtomicInteger concurrently: " + finalScore);
        
        if (finalScore == totalBalls) {
            System.out.println("SUCCESS: No race conditions detected during concurrent score accumulation.");
        } else {
            System.out.println("FAILED: Race condition detected. Score mismatch.");
        }
    }

    public static void main(String[] args) {
        run();
    }
}
