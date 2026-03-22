package chessgame_sde2;

public class Player {
    private final String id;
    private final Color color;

    public Player(String id, Color color) {
        this.id = id;
        this.color = color;
    }

    public String getId() { return id; }
    public Color getColor() { return color; }
}
