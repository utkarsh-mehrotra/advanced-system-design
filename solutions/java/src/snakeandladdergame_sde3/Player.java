package snakeandladdergame_sde3;

import java.util.concurrent.atomic.AtomicInteger;

public class Player {
    private final String id;
    private final AtomicInteger position;

    public Player(String id) {
        this.id = id;
        this.position = new AtomicInteger(0);
    }

    public String getId() { return id; }
    public int getPosition() { return position.get(); }

    public void updatePosition(int newPosition) {
        this.position.set(newPosition);
        EventBus.getInstance().publish("PLAYER_MOVED", id + ":" + newPosition);
    }
}
