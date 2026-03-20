package chessgame_upgraded;

import chessgame_upgraded.pieces.Piece;

public class Move {
    private final Piece piece;
    private final int sourceRow;
    private final int sourceCol;
    private final int destRow;
    private final int destCol;
    
    // We capture the piece destroyed if any, useful for Undo rollbacks during Check analysis
    private Piece capturedPiece;

    public Move(Piece piece, int destRow, int destCol) {
        this.piece = piece;
        this.sourceRow = piece.getRow();
        this.sourceCol = piece.getCol();
        this.destRow = destRow;
        this.destCol = destCol;
    }

    public Piece getPiece() { return piece; }
    public int getSourceRow() { return sourceRow; }
    public int getSourceCol() { return sourceCol; }
    public int getDestRow() { return destRow; }
    public int getDestCol() { return destCol; }
    public void setCapturedPiece(Piece capturedPiece) { this.capturedPiece = capturedPiece; }
    public Piece getCapturedPiece() { return capturedPiece; }
}
