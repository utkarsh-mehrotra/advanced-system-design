package elevatorsystem_sde2.strategy;

import elevatorsystem_sde2.Elevator;
import elevatorsystem_sde2.Request;

import java.util.List;

/**
 * SDE3: Strategy Pattern Interface for decoupled dispatch mechanics.
 */
public interface ElevatorSelectionStrategy {
    Elevator selectElevator(List<Elevator> elevators, Request request);
}
