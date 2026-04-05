package tictactoe_sde3;

import java.util.concurrent.atomic.AtomicReference;

public class BoardCell {
    private final AtomicReference<Character> piece;

    public BoardCell() {
        this.piece = new AtomicReference<>('-');
    }

    public boolean claimCell(char newPiece) {
        return piece.compareAndSet('-', newPiece);
    }

    public char getPiece() {
        return piece.get();
    }
}
