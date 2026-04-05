package airlinemanagementsystem_sde3;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BookingManager {
    private final Map<String, Flight> flightInventory = new ConcurrentHashMap<>();

    public void addFlight(Flight flight) {
        flightInventory.put(flight.getFlightNumber(), flight);
    }

    public void transitionFlightAsync(String flightNumber, FlightState expected, FlightState next) {
        Flight f = flightInventory.get(flightNumber);
        if (f != null && f.transitionState(expected, next)) {
            // Emitting to event bus allows Baggage handlers, Notifiers, Gate dispatchers to react lock-free
            EventBus.getInstance().publish("FLIGHT_STATE_CHANGED", flightNumber + ":" + next.name());
        }
    }
}
