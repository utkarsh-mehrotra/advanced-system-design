package cricinfo_sde3;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Over {
    private final int overNumber;
    private final List<Ball> balls;

    public Over(int overNumber) {
        this.overNumber = overNumber;
        this.balls = new CopyOnWriteArrayList<>(); // Thread-safe iterative appends
    }

    public void addBall(Ball ball) {
        this.balls.add(ball);
    }

    public int getOverNumber() {
        return overNumber;
    }

    public List<Ball> getBalls() {
        return balls;
    }
}
