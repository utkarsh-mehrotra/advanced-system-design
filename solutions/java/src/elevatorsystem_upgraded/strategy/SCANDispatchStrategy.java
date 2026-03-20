package elevatorsystem_upgraded.strategy;

import elevatorsystem_upgraded.Direction;
import elevatorsystem_upgraded.Elevator;
import elevatorsystem_upgraded.Request;

import java.util.List;

/**
 * Direction-Aware SCAN Dispatcher.
 * Selects an elevator not just based on absolute physical distance, but heavily 
 * weighs the Direction vector. An elevator moving towards the passenger on the same path 
 * gets massive priority over one moving away.
 */
public class SCANDispatchStrategy implements ElevatorSelectionStrategy {

    @Override
    public Elevator selectElevator(List<Elevator> elevators, Request request) {
        Elevator bestElevator = null;
        int minCost = Integer.MAX_VALUE;

        int requestSource = request.getSourceFloor();
        Direction requestDirection = (request.getDestinationFloor() > requestSource) ? Direction.UP : Direction.DOWN;

        for (Elevator elevator : elevators) {
            int currentFloor = elevator.getCurrentFloor();
            Direction currentDirection = elevator.getCurrentDirection();
            
            // Calculate base physical distance
            int distance = Math.abs(currentFloor - requestSource);
            int cost = distance;

            // Apply directional modifiers
            if (currentDirection == Direction.IDLE) {
                // Idle elevators perfectly inherit the exact distance as cost
                cost = distance;
            } else if (currentDirection == Direction.UP) {
                if (requestDirection == Direction.UP && requestSource >= currentFloor) {
                    // Perfect interception path! Elevator is below us and coming UP.
                    cost = distance;
                } else {
                    // Elevator is moving UP but has either passed us, or we want to go DOWN. 
                    // Heavily penalize this so we don't assign it unless it's the only option.
                    cost = distance + 100; // Simulated loop-around penalty
                }
            } else if (currentDirection == Direction.DOWN) {
                if (requestDirection == Direction.DOWN && requestSource <= currentFloor) {
                    // Perfect interception path! Elevator is above us and coming DOWN.
                    cost = distance;
                } else {
                    // Elevator passed us or we want to go opposite
                    cost = distance + 100;
                }
            }

            if (cost < minCost) {
                minCost = cost;
                bestElevator = elevator;
            }
        }

        return bestElevator;
    }
}
