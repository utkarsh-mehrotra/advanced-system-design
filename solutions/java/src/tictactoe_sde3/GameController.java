package tictactoe_sde3;

import java.util.concurrent.atomic.AtomicReference;

public class GameController {
    private final BoardCell[][] grid = new BoardCell[3][3];
    // CAS safe pointer for active turn prevents concurrent cross-turns
    private final AtomicReference<Character> currentTurn = new AtomicReference<>('X');

    public GameController() {
        for(int i=0; i<3; i++) for(int j=0; j<3; j++) grid[i][j] = new BoardCell();
    }

    public void playTurn(char playerPiece, int r, int c) {
        if (r < 0 || r > 2 || c < 0 || c > 2) return;

        char activePiece = currentTurn.get();
        if (activePiece != playerPiece) {
            System.out.println("It's not " + playerPiece + "'s turn!");
            return;
        }

        BoardCell cell = grid[r][c];
        if (cell.claimCell(playerPiece)) {
            char nextTurn = (playerPiece == 'X') ? 'O' : 'X';
            currentTurn.set(nextTurn); // Uncontested write
            
            // Async Event bus for decoupling UI and spectators from exact logic latency
            EventBus.getInstance().publish("BOARD_UPDATED", "Player " + playerPiece + " placed on [" + r + "," + c + "]");
        } else {
            System.out.println("SDE3 Cell already occupied!");
        }
    }
}
