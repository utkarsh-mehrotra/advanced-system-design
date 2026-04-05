package chessgame_sde3;

public class PlayerNotificationService {
    public PlayerNotificationService() {
        EventBus.getInstance().subscribe("MOVE_PLAYED", this::notifyMove);
        EventBus.getInstance().subscribe("GAME_OVER", this::notifyEnd);
    }

    private void notifyMove(Object payload) {
        System.out.println("WebSocket Fan-out: Animating move -> " + payload);
    }

    private void notifyEnd(Object payload) {
        System.out.println("WebSocket Fan-out: Show Game Over Screen -> " + payload);
    }
}
