package chessgame_sde3;

import chessgame_sde3.pieces.King;
import chessgame_sde3.pieces.Pawn;
import chessgame_sde3.pieces.Piece;

import java.util.ArrayList;
import java.util.List;

/**
 * SDE3 Check/Checkmate Mathematics Rule Engine.
 * Decoupled from the physical Board bounds. Replaces the infinite Scanner loop.
 */
public class GameEngine {
    private final Board board;
    private final Player[] players;
    private int currentPlayerIndex;
    private GameState state;

    public GameEngine(Player p1, Player p2) {
        this.board = new Board();
        this.players = new Player[]{p1, p2};
        this.currentPlayerIndex = 0;
        this.state = GameState.ACTIVE;
    }

    public synchronized boolean processMove(Player player, int srcR, int srcC, int dstR, int dstC) {
        if (state != GameState.ACTIVE) {
            System.out.println("Game is completed. State: " + state);
            return false;
        }
        if (!player.getId().equals(players[currentPlayerIndex].getId())) {
            System.out.println("Wait for your turn, " + player.getId());
            return false;
        }

        Piece piece = board.getPiece(srcR, srcC);
        if (piece == null || piece.getColor() != player.getColor()) {
            return false;
        }

        // Validate Mathematics and Pathing Constraints
        if (!isGloballyValidMove(piece, dstR, dstC)) {
            System.out.println("Invalid topological piece movement constraints.");
            return false;
        }

        // SIMULATED ROLLBACK: "Does this move put my own King in danger?"
        Move command = new Move(piece, dstR, dstC);
        board.executeMove(command);
        if (isKingInCheck(player.getColor())) {
            board.rollbackMove(command);
            System.out.println("Invalid Rule Error: Move places your King directly into Check.");
            return false;
        }
        
        System.out.println("Validated " + piece.getClass().getSimpleName() + " move from [" + srcR + "," + srcC + "] -> [" + dstR + "," + dstC + "]");

        // Finalize state changes!
        currentPlayerIndex = (currentPlayerIndex + 1) % 2;
        Color nextColor = players[currentPlayerIndex].getColor();

        // After finalizing the execution, we check if the NEXT player is completely trapped.
        if (isCheckmate(nextColor)) {
            state = player.getColor() == Color.WHITE ? GameState.WHITE_WIN : GameState.BLACK_WIN;
            System.out.println("CHECKMATE! " + state);
        }

        return true;
    }

    private boolean isGloballyValidMove(Piece piece, int dstR, int dstC) {
        // Boundary Logic
        if (dstR < 0 || dstR > 7 || dstC < 0 || dstC > 7) return false;
        
        Piece destinationEntity = board.getPiece(dstR, dstC);
        // Friendly Fire Check
        if (destinationEntity != null && destinationEntity.getColor() == piece.getColor()) return false;
        
        // Pawn special diagonal kill mechanics vs straight movement
        if (piece instanceof Pawn) {
            boolean isDiagonal = (piece.getCol() - dstC) != 0;
            if (isDiagonal && destinationEntity == null) return false; // Pawns can only move diagonal iteratively to kill
            if (!isDiagonal && destinationEntity != null) return false; // Pawns cannot move straight if blocked
        }

        // Mathematical shape geometry
        if (!piece.canMove(dstR, dstC)) return false;

        // Path collision for Bishops/Rooks/Queens
        if (piece.isSlidingPiece()) {
            if (!board.isValidPath(piece.getRow(), piece.getCol(), dstR, dstC)) return false;
        }

        return true;
    }

    private boolean isKingInCheck(Color kingColor) {
        // Find King mathematically
        int kr = -1, kc = -1;
        
        board.getLock().readLock().lock();
        try {
            for (int r = 0; r < 8; r++) {
                for (int c = 0; c < 8; c++) {
                    Piece p = board.getPiece(r, c);
                    if (p instanceof King && p.getColor() == kingColor) {
                        kr = r; kc = c;
                    }
                }
            }
            
            // Can any enemy legally jump there?
            Color enemyColor = (kingColor == Color.WHITE) ? Color.BLACK : Color.WHITE;
            for (int r = 0; r < 8; r++) {
                for (int c = 0; c < 8; c++) {
                    Piece enemy = board.getPiece(r, c);
                    if (enemy != null && enemy.getColor() == enemyColor) {
                        if (isGloballyValidMove(enemy, kr, kc)) {
                            return true; // Mathematical Check identified
                        }
                    }
                }
            }
        } finally {
            board.getLock().readLock().unlock();
        }
        return false;
    }

    private boolean isCheckmate(Color defenderColor) {
        // A Checkmate is if you are currently IN Check, and there is NO mathematically physically possible 
        // move combination on the board that removes you from Check.
        if (!isKingInCheck(defenderColor)) return false;

        board.getLock().writeLock().lock();
        try {
            for (int r = 0; r < 8; r++) {
                for (int c = 0; c < 8; c++) {
                    Piece p = board.getPiece(r, c);
                    if (p != null && p.getColor() == defenderColor) {
                        
                        // Brute Force Mathematical Sandbox Check generator
                        for (int dr = 0; dr < 8; dr++) {
                            for (int dc = 0; dc < 8; dc++) {
                                if (isGloballyValidMove(p, dr, dc)) {
                                    Move rollbackSandboxMove = new Move(p, dr, dc);
                                    board.executeMove(rollbackSandboxMove);
                                    boolean stillInCheck = isKingInCheck(defenderColor);
                                    board.rollbackMove(rollbackSandboxMove);

                                    // If even ONE move brings us back to safety, it's NOT Checkmate.
                                    if (!stillInCheck) return false; 
                                }
                            }
                        }
                    }
                }
            }
        } finally {
            board.getLock().writeLock().unlock();
        }

        return true;
    }
}
