package snakeandladdergame_upgraded;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class GameManager {
    private final ExecutorService gameEnginePool;
    // Map to hold running games and their futures so we can query status or cancel them
    private final Map<String, Future<Player>> activeGames; 

    public GameManager() {
        // Support running multiple games in parallel
        this.gameEnginePool = Executors.newCachedThreadPool();
        this.activeGames = new ConcurrentHashMap<>();
    }

    public String startNewGame(List<String> playerNames) {
        String gameId = UUID.randomUUID().toString().substring(0, 8);
        SnakeAndLadderGame game = new SnakeAndLadderGame(gameId, playerNames);
        
        System.out.println("GameManager: Dispatching Game " + gameId + " to Engine Pool.");
        Future<Player> gameFuture = gameEnginePool.submit(game);
        
        activeGames.put(gameId, gameFuture);
        return gameId;
    }

    public Future<Player> getGameFuture(String gameId) {
        return activeGames.get(gameId);
    }

    public void shutdown() {
        gameEnginePool.shutdownNow();
    }
}
