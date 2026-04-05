package tictactoe_sde3;

public class TicTacToeDemoSDE3 {
    public static void main(String[] args) {
        new SpectatorView();

        GameController controller = new GameController();
        
        System.out.println("Player X moves...");
        controller.playTurn('X', 0, 0);

        System.out.println("Player X attempts concurrent double-tap hack...");
        controller.playTurn('X', 0, 1);

        System.out.println("Player O moves validly...");
        controller.playTurn('O', 1, 1);
    }
}
