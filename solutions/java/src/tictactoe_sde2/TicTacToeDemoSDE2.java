package tictactoe_sde2;

public class TicTacToeDemoSDE2 {
    public static void main(String[] args) {
        Player p1 = new Player("P1", 'X');
        Player p2 = new Player("P2", 'O');
        
        GameController game = new GameController(p1, p2);
        
        System.out.println("Player 1 moves...");
        game.playTurn(p1, 0, 0);
        
        System.out.println("Player 2 moves...");
        game.playTurn(p2, 1, 1);
        
        System.out.println("Player 2 tries moving out of turn...");
        game.playTurn(p2, 0, 1);
    }
}
