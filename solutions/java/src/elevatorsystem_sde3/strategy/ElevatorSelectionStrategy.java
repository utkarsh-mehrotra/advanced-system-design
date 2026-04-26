package elevatorsystem_sde3.strategy;

import elevatorsystem_sde3.Elevator;
import elevatorsystem_sde3.Request;

import java.util.List;

/**
 * SDE3: Strategy Pattern Interface for decoupled dispatch mechanics.
 */
public interface ElevatorSelectionStrategy {
    Elevator selectElevator(List<Elevator> elevators, Request request);
}
