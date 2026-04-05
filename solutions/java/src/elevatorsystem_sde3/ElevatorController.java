package elevatorsystem_sde3;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ElevatorController {
    private final Map<String, Elevator> elevators = new ConcurrentHashMap<>();

    public ElevatorController() {
        // Asynchronously listen to physical sensor trips
        EventBus.getInstance().subscribe("ELEVATOR_REACHED_FLOOR", this::handleFloorReached);
    }

    public void registerElevator(Elevator elevator) {
        elevators.put(elevator.getId(), elevator);
    }

    public void requestElevator(int sourceFloor, int destinationFloor) {
        System.out.println("ElevatorController: Computing optimal assignment...");
        Elevator chosen = elevators.values().iterator().next(); // simplified 
        chosen.dispatch(Direction.UP);
    }

    private void handleFloorReached(Object payload) {
        String data = (String) payload;
        System.out.println("ElevatorController [Async Sensor]: " + data + ". Processing stop checks.");
    }
}
