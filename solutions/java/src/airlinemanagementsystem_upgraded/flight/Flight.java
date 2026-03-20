package airlinemanagementsystem_upgraded.flight;

import airlinemanagementsystem_upgraded.Aircraft;
import airlinemanagementsystem_upgraded.seat.Seat;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class Flight {
    private final String flightNumber;
    private final Aircraft aircraft;
    private final String source;
    private final String destination;
    private final LocalDateTime departureTime;
    private final LocalDateTime arrivalTime;
    // Immutable list, but the elements (Seats) themselves handle their own thread-safety internally.
    private final List<Seat> seats;

    public Flight(String flightNumber, Aircraft aircraft, String source, String destination, 
                  LocalDateTime departureTime, LocalDateTime arrivalTime, List<Seat> seats) {
        this.flightNumber = flightNumber;
        this.aircraft = aircraft;
        this.source = source;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        // Make defensive copy wrapped in unmodifiable to prevent list mutation
        this.seats = Collections.unmodifiableList(seats);
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public Aircraft getAircraft() {
        return aircraft;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    /**
     * Returns an immutable view of all seats.
     * The seats themselves manage their reservation state concurrently.
     */
    public List<Seat> getSeats() {
        return seats;
    }

    /**
     * Gets available seats at a specific point in time.
     * Note: A seat might become reserved immediately after this call (TOCTOU).
     * Clients must call `seat.reserve()` to guarantee placement.
     */
    public List<Seat> getAvailableSeats() {
        return seats.stream()
                .filter(Seat::isAvailable)
                .toList(); 
    }
}
