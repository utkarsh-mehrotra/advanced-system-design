package chessgame_sde2.pieces;

import chessgame_sde2.Color;

public class Pawn extends Piece {

    public Pawn(Color color, int row, int col) {
        super(color, row, col);
    }

    @Override
    public boolean canMove(int destRow, int destCol) {
        int rowDiff = destRow - row;
        int colDiff = Math.abs(destCol - col);

        if (color == Color.WHITE) {
            // White moves UP (+1)
            // Forward
            if (colDiff == 0 && rowDiff == 1) return true;
            // Double first move
            if (row == 1 && colDiff == 0 && rowDiff == 2) return true;
            // Capture diagonal
            if (colDiff == 1 && rowDiff == 1) return true; // (Checking if piece actually exists is delegated to Board)
        } else {
            // Black moves DOWN (-1)
            if (colDiff == 0 && rowDiff == -1) return true;
            if (row == 6 && colDiff == 0 && rowDiff == -2) return true;
            if (colDiff == 1 && rowDiff == -1) return true;
        }

        return false;
    }
}
