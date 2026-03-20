package chessgame_upgraded.pieces;

import chessgame_upgraded.Board;
import chessgame_upgraded.Color;

public abstract class Piece {
    protected final Color color;
    protected int row;
    protected int col;

    public Piece(Color color, int row, int col) {
        this.color = color;
        this.row = row;
        this.col = col;
    }

    /**
     * SDE3: Pieces only evaluate their math scalar geometry.
     * Topological pathing ray-casts (like checking if a teammate is in the way) are executed by the Board.
     */
    public abstract boolean canMove(int destRow, int destCol);

    public boolean isSlidingPiece() {
        return false;
    }

    public Color getColor() { return color; }
    public int getRow() { return row; }
    public int getCol() { return col; }

    public void setRow(int row) { this.row = row; }
    public void setCol(int col) { this.col = col; }
}
