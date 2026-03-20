package elevatorsystem_upgraded;

import elevatorsystem_upgraded.strategy.ElevatorSelectionStrategy;

import java.util.ArrayList;
import java.util.List;

public class ElevatorController {
    private final List<Elevator> elevators;
    private final ElevatorSelectionStrategy selectionStrategy;

    public ElevatorController(int numElevators, int capacity, ElevatorSelectionStrategy strategy) {
        this.selectionStrategy = strategy;
        this.elevators = new ArrayList<>();
        
        for (int i = 0; i < numElevators; i++) {
            Elevator elevator = new Elevator(i + 1, capacity);
            elevators.add(elevator);
            // SDE3: Good practice to keep thread references if we ever needed graceful shutdown, but this is fine for demo
            new Thread(elevator, "ElevatorThread-" + (i + 1)).start();
        }
    }

    public void requestElevator(int sourceFloor, int destinationFloor) {
        Request request = new Request(sourceFloor, destinationFloor);
        
        // SDE3: Utilizing the decoupled strategy pattern instance!
        Elevator optimalElevator = selectionStrategy.selectElevator(elevators, request);
        
        if (optimalElevator != null) {
            System.out.println("[Controller] Assigned passenger request (" + sourceFloor + " -> " + destinationFloor + ") to Elevator " + optimalElevator.getCurrentFloor() + " (moving " + optimalElevator.getCurrentDirection() + ")");
            optimalElevator.addRequest(request);
        } else {
            System.out.println("[Controller] System overloaded, no elevators available.");
        }
    }
}
