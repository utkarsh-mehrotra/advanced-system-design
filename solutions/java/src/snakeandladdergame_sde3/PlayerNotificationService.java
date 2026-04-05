package snakeandladdergame_sde3;

public class PlayerNotificationService {
    public PlayerNotificationService() {
        EventBus.getInstance().subscribe("PLAYER_MOVED", this::notifyMove);
        EventBus.getInstance().subscribe("GAME_WON", this::notifyWin);
    }

    private void notifyMove(Object payload) {
        System.out.println("WebSocket UI [Async View]: Animating piece -> " + payload);
    }

    private void notifyWin(Object payload) {
        System.out.println("WebSocket UI [Async View]: Displaying Victory Screen for -> " + payload);
    }
}
