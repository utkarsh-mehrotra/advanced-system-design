package chessgame_sde3;

public class ChessDemoSDE3 {
    public static void main(String[] args) {
        new PlayerNotificationService();

        GameController controller = new GameController("CHESS_101");
        
        System.out.println("Starting Match...");
        controller.recordMoveAsync("e2e4");
        controller.recordMoveAsync("e7e5");
        
        controller.forceStateEnd(GameState.WHITE_WIN);
    }
}
