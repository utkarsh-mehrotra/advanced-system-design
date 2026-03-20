package elevatorsystem_upgraded.strategy;

import elevatorsystem_upgraded.Elevator;
import elevatorsystem_upgraded.Request;

import java.util.List;

/**
 * SDE3: Strategy Pattern Interface for decoupled dispatch mechanics.
 */
public interface ElevatorSelectionStrategy {
    Elevator selectElevator(List<Elevator> elevators, Request request);
}
