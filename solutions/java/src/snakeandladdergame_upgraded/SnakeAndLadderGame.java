package snakeandladdergame_upgraded;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class SnakeAndLadderGame implements Callable<Player> {
    private final String gameId;
    private final Board board;
    private final List<Player> players;
    private final Dice dice;
    private int currentPlayerIndex;

    public SnakeAndLadderGame(String gameId, List<String> playerNames) {
        this.gameId = gameId;
        this.board = new Board();
        this.dice = new Dice();
        this.players = new ArrayList<>();
        for (String playerName : playerNames) {
            players.add(new Player(playerName));
        }
        this.currentPlayerIndex = 0;
    }

    @Override
    public Player call() {
        System.out.println("Started Game: " + gameId + " with " + players.size() + " players.");
        try {
            while (!isGameOver()) {
                Player currentPlayer = players.get(currentPlayerIndex);
                int diceRoll = dice.roll();
                int newPosition = currentPlayer.getPosition() + diceRoll;

                if (newPosition <= board.getBoardSize()) {
                    int finalPosition = board.getNewPositionAfterSnakeOrLadder(newPosition);
                    currentPlayer.setPosition(finalPosition);
                    // System.out.println("["+gameId+"] " + currentPlayer.getName() + " rolled " + diceRoll + " -> pos " + finalPosition);
                }

                if (currentPlayer.getPosition() == board.getBoardSize()) {
                    System.out.println("["+gameId+"] " + currentPlayer.getName() + " WINS!");
                    return currentPlayer;
                }

                currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
                
                // Simulate human thinking/rolling time for concurrency demonstration
                Thread.sleep(10); 
            }
        } catch (InterruptedException e) {
            System.out.println("Game " + gameId + " interrupted.");
            Thread.currentThread().interrupt();
        }
        return null; // Should not reach here ordinarily
    }

    private boolean isGameOver() {
        for (Player player : players) {
            if (player.getPosition() == board.getBoardSize()) {
                return true;
            }
        }
        return false;
    }

    public String getGameId() {
        return gameId;
    }
}
