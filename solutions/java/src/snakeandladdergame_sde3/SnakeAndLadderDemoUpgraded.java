package snakeandladdergame_sde3;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class SnakeAndLadderDemoUpgraded {
    public static void run() {
        GameManager gameManager = new GameManager();

        // Start multiple games in parallel on the same thread pool
        List<String> players1 = Arrays.asList("Alice", "Bob", "Charlie");
        String game1Id = gameManager.startNewGame(players1);

        List<String> players2 = Arrays.asList("David", "Eve");
        String game2Id = gameManager.startNewGame(players2);

        // Fetch Futures representing the game computations
        Future<Player> game1Future = gameManager.getGameFuture(game1Id);
        Future<Player> game2Future = gameManager.getGameFuture(game2Id);

        try {
            System.out.println("Waiting for Game 1 to finish...");
            Player winner1 = game1Future.get(); // Blocks until game 1 completes
            System.out.println("\n*** Game 1 Official Winner: " + winner1.getName() + " ***\n");

            System.out.println("Waiting for Game 2 to finish...");
            Player winner2 = game2Future.get(); // Blocks until game 2 completes
            System.out.println("\n*** Game 2 Official Winner: " + winner2.getName() + " ***\n");

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            gameManager.shutdown();
        }
    }

    public static void main(String[] args) {
        run();
    }
}
