package chessgame_sde2.pieces;

import chessgame_sde2.Color;

public class Bishop extends Piece {

    public Bishop(Color color, int row, int col) {
        super(color, row, col);
    }

    @Override
    public boolean canMove(int destRow, int destCol) {
        int rowDiff = Math.abs(destRow - row);
        int colDiff = Math.abs(destCol - col);
        return rowDiff == colDiff;
    }

    @Override
    public boolean isSlidingPiece() {
        return true;
    }
}
