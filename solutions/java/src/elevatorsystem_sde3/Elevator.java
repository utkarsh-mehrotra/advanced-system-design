package elevatorsystem_sde3;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Elevator {
    private final String id;
    private final AtomicInteger currentFloor;
    private final AtomicReference<Direction> currentDirection;
    private final AtomicReference<ElevatorState> state;

    public Elevator(String id) {
        this.id = id;
        this.currentFloor = new AtomicInteger(0);
        this.currentDirection = new AtomicReference<>(Direction.STOPPED);
        this.state = new AtomicReference<>(ElevatorState.IDLE);
    }

    public String getId() { return id; }
    
    // CAS mechanics for floor updates simulating physical sensor trips
    public void sensorReachedFloor(int floor) {
        currentFloor.set(floor); // Hard set based on sensor
        EventBus.getInstance().publish("ELEVATOR_REACHED_FLOOR", id + ":" + floor);
    }

    public void dispatch(Direction dir) {
        currentDirection.set(dir);
        state.compareAndSet(ElevatorState.IDLE, ElevatorState.MOVING);
    }
}
