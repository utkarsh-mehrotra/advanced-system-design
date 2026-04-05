package tictactoe_sde2;

public class Player {
    private final String id;
    private final char piece;

    public Player(String id, char piece) {
        this.id = id;
        this.piece = piece;
    }

    public String getId() { return id; }
    public char getPiece() { return piece; }
}
