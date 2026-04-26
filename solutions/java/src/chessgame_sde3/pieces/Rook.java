package chessgame_sde3.pieces;

import chessgame_sde3.Color;

public class Rook extends Piece {
    public Rook(Color color, int row, int col) {
        super(color, row, col);
    }

    @Override
    public boolean canMove(int destRow, int destCol) {
        return this.row == destRow || this.col == destCol;
    }

    @Override
    public boolean isSlidingPiece() {
        return true;
    }
}
