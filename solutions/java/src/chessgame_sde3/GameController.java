package chessgame_sde3;

import java.util.concurrent.atomic.AtomicReference;

public class GameController {
    private final String gameId;
    private final AtomicReference<GameState> state;

    public GameController(String gameId) {
        this.gameId = gameId;
        this.state = new AtomicReference<>(GameState.ACTIVE);
    }

    public GameState getState() { return state.get(); }

    public void recordMoveAsync(String uciMove) {
        if (state.get() != GameState.ACTIVE) return;
        
        System.out.println("Processing UCI Move: " + uciMove);
        EventBus.getInstance().publish("MOVE_PLAYED", gameId + ":" + uciMove);
    }
    
    public void forceStateEnd(GameState finalState) {
        if (state.compareAndSet(GameState.ACTIVE, finalState)) {
            EventBus.getInstance().publish("GAME_OVER", gameId + ":" + finalState.name());
        }
    }
}
