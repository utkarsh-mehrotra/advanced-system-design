package tictactoe_sde3;

import java.util.concurrent.locks.ReentrantLock;

public class GameController {
    private final Board board = new Board();
    private final Player p1;
    private final Player p2;
    private Player currentTurn;
    
    private final ReentrantLock lock = new ReentrantLock();

    public GameController(Player p1, Player p2) {
        this.p1 = p1;
        this.p2 = p2;
        this.currentTurn = p1;
    }

    public void playTurn(Player player, int r, int c) {
        lock.lock();
        try {
            if (player != currentTurn) {
                System.out.println("It's not " + player.getId() + "'s turn!");
                return;
            }
            if (board.placePiece(r, c, player.getPiece())) {
                System.out.println("SDE2: Move registered.");
                board.printBoard();
                currentTurn = (currentTurn == p1) ? p2 : p1;
            } else {
                System.out.println("SDE2: Invalid move.");
            }
        } finally {
            lock.unlock();
        }
    }
}
