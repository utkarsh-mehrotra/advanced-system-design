package airlinemanagementsystem_sde3;

import java.util.concurrent.atomic.AtomicReference;

public class Flight {
    private final String flightNumber;
    private final AtomicReference<FlightState> state;

    public Flight(String flightNumber) {
        this.flightNumber = flightNumber;
        this.state = new AtomicReference<>(FlightState.SCHEDULED);
    }

    public String getFlightNumber() { return flightNumber; }
    public FlightState getState() { return state.get(); }

    public boolean transitionState(FlightState expected, FlightState next) {
        return state.compareAndSet(expected, next);
    }
}
