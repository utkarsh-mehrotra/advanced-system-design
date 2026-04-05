package snakeandladdergame_sde3;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class GameController {
    private final List<Player> players;
    private final AtomicReference<GameState> state;

    public GameController(List<Player> players) {
        this.players = players;
        this.state = new AtomicReference<>(GameState.NOT_STARTED);
    }

    public void startGame() {
        if (state.compareAndSet(GameState.NOT_STARTED, GameState.IN_PROGRESS)) {
            System.out.println("GameController: Game initialization sequence triggered lock-free.");
        }
    }

    public void processMove(Player player, int diceRoll) {
        if (state.get() != GameState.IN_PROGRESS) return;

        int newPosition = player.getPosition() + diceRoll;
        // Simplified Logic assuming fixed map 1-100 without snake jumps for demo bounds
        if (newPosition >= 100) {
            newPosition = 100;
            if (state.compareAndSet(GameState.IN_PROGRESS, GameState.COMPLETED)) {
                EventBus.getInstance().publish("GAME_WON", player.getId());
            }
        }
        player.updatePosition(newPosition);
    }
}
