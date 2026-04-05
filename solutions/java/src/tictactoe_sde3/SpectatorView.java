package tictactoe_sde3;

public class SpectatorView {
    public SpectatorView() {
        EventBus.getInstance().subscribe("BOARD_UPDATED", this::pushWebSocket);
    }

    private void pushWebSocket(Object payload) {
        System.out.println("SpectatorView [Async WebSocket]: Rendering global update -> " + payload);
    }
}
