package chessgame_upgraded;

import chessgame_upgraded.pieces.*;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Pure Domain Topological 2D Array.
 * SDE3: Unlike the junior LLD, this does NOT contain game-ending rules. It only stores Physical Geometry
 * and explicitly calculates 2D ray-casting collisions for sliding pieces.
 */
public class Board {
    private final Piece[][] grid;
    
    // SDE3: A ReadWriteLock allows 10,000 spectators to iterate the board concurrently.
    // However, when a Move executes, it grabs the WRITE lock, completely freezing spectator reads
    // so they don't see half-computed 'Ghost' board states mid-rollback transaction.
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public Board() {
        grid = new Piece[8][8];
        initialize();
    }

    private void initialize() {
        // Base setup... White
        grid[0][0] = new Rook(Color.WHITE, 0, 0);
        grid[0][1] = new Knight(Color.WHITE, 0, 1);
        grid[0][2] = new Bishop(Color.WHITE, 0, 2);
        grid[0][3] = new Queen(Color.WHITE, 0, 3);
        grid[0][4] = new King(Color.WHITE, 0, 4);
        grid[0][5] = new Bishop(Color.WHITE, 0, 5);
        grid[0][6] = new Knight(Color.WHITE, 0, 6);
        grid[0][7] = new Rook(Color.WHITE, 0, 7);
        for (int i = 0; i < 8; i++) grid[1][i] = new Pawn(Color.WHITE, 1, i);

        // Black
        grid[7][0] = new Rook(Color.BLACK, 7, 0);
        grid[7][1] = new Knight(Color.BLACK, 7, 1);
        grid[7][2] = new Bishop(Color.BLACK, 7, 2);
        grid[7][3] = new Queen(Color.BLACK, 7, 3);
        grid[7][4] = new King(Color.BLACK, 7, 4);
        grid[7][5] = new Bishop(Color.BLACK, 7, 5);
        grid[7][6] = new Knight(Color.BLACK, 7, 6);
        grid[7][7] = new Rook(Color.BLACK, 7, 7);
        for (int i = 0; i < 8; i++) grid[6][i] = new Pawn(Color.BLACK, 6, i);
    }

    public Piece getPiece(int row, int col) {
        lock.readLock().lock();
        try {
            return grid[row][col];
        } finally {
            lock.readLock().unlock();
        }
    }

    public void setPiece(int row, int col, Piece piece) {
        // Unsafe internally to avoid lock escalation overhead during batch rule simulation.
        // Protected externally by executingMove block.
        grid[row][col] = piece;
        if (piece != null) {
            piece.setRow(row);
            piece.setCol(col);
        }
    }

    /**
     * Executes the topological move blindly.
     * Note: SDE3 separates validity engine from movement.
     */
    public void executeMove(Move move) {
        lock.writeLock().lock();
        try {
            Piece target = grid[move.getDestRow()][move.getDestCol()];
            move.setCapturedPiece(target); // Save state for simulated mathematical rollbacks

            setPiece(move.getDestRow(), move.getDestCol(), move.getPiece());
            setPiece(move.getSourceRow(), move.getSourceCol(), null);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void rollbackMove(Move move) {
        lock.writeLock().lock();
        try {
            setPiece(move.getSourceRow(), move.getSourceCol(), move.getPiece());
            setPiece(move.getDestRow(), move.getDestCol(), move.getCapturedPiece());
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * SDE3: Missing Collision Logic added!
     * Calculates the vector delta between start/end mathematically, scanning every physical tile between.
     */
    public boolean isValidPath(int startRow, int startCol, int endRow, int endCol) {
        int rowDir = Integer.compare(endRow, startRow);
        int colDir = Integer.compare(endCol, startCol);

        int r = startRow + rowDir;
        int c = startCol + colDir;

        // Ray-Casting Iteration
        while (r != endRow || c != endCol) {
            if (grid[r][c] != null) {
                return false; // Physical Obstruction found!
            }
            r += rowDir;
            c += colDir;
        }
        return true;
    }

    public ReadWriteLock getLock() {
        return lock;
    }
}
