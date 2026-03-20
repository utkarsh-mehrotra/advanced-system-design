package chessgame_upgraded.pieces;

import chessgame_upgraded.Color;

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
