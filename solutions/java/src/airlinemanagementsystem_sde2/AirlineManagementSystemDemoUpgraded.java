package airlinemanagementsystem_sde2;

import airlinemanagementsystem_sde2.booking.Booking;
import airlinemanagementsystem_sde2.booking.BookingService;
import airlinemanagementsystem_sde2.booking.BookingServiceImpl;
import airlinemanagementsystem_sde2.flight.DefaultFlightSearchService;
import airlinemanagementsystem_sde2.flight.Flight;
import airlinemanagementsystem_sde2.flight.FlightSearchService;
import airlinemanagementsystem_sde2.payment.PaymentService;
import airlinemanagementsystem_sde2.payment.StripePaymentStrategy;
import airlinemanagementsystem_sde2.seat.Seat;
import airlinemanagementsystem_sde2.seat.SeatType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AirlineManagementSystemDemoUpgraded {
    public static void main(String[] args) {
        // ---- 1. SETUP: Bootstrap with Dependency Injection Context -----
        List<Flight> flightDatabase = new ArrayList<>();
        FlightSearchService flightSearch = new DefaultFlightSearchService(flightDatabase);
        BookingService bookingService = new BookingServiceImpl();
        PaymentService stripePayment = new StripePaymentStrategy();
        
        // Inject dependencies into Facade (replaces old Singletons)
        AirlineManagementFacade facade = new AirlineManagementFacade(flightSearch, bookingService, stripePayment);

        // ---- 2. Data Population -----
        Passenger p1 = new Passenger("U001", "John Doe", "john@example.com", "1234567890");
        Passenger p2 = new Passenger("U002", "Jane Smith", "jane@example.com", "0987654321");

        Aircraft aircraft1 = new Aircraft("A001", "Boeing 747", 100);

        List<Seat> flight1Seats = new ArrayList<>();
        Seat seat1 = new Seat("12A", SeatType.ECONOMY);
        Seat seat2 = new Seat("12B", SeatType.ECONOMY);
        flight1Seats.add(seat1);
        flight1Seats.add(seat2);

        LocalDateTime dep = LocalDateTime.now().plusDays(1);
        Flight flight1 = new Flight("FL101", aircraft1, "JFK", "LHR", dep, dep.plusHours(6), flight1Seats);
        flightDatabase.add(flight1);

        // ---- 3. Execute Core Flows -----
        System.out.println("--- Searching Flights ---");
        List<Flight> searchResults = facade.searchFlights("JFK", "LHR", LocalDate.now().plusDays(1));
        searchResults.forEach(f -> System.out.println("Found Flight: " + f.getFlightNumber()));

        System.out.println("\n--- Proceeding to Book ---");
        try {
            // Success Scenario
            BigDecimal price = new BigDecimal("450.50");
            Booking b1 = facade.bookFlight(flight1, p1, seat1, price);
            
            // Simulating concurrency contention: p2 tries to book the SAME seat exactly when p1 booked it.
            // (But p1 already reserved it atomically, so an exception should be thrown to prevent overbooking)
            System.out.println("\n--- Simulating Overbooking Attempt ---");
            Booking b2 = facade.bookFlight(flight1, p2, seat1, price); 
        } catch (Exception e) {
            System.err.println("Caught Expected System Exception: " + e.getMessage());
        }
        
        System.out.println("\n--- Booking another available seat ---");
        Booking b3 = facade.bookFlight(flight1, p2, seat2, new BigDecimal("450.50"));

        System.out.println("\n--- Cancel Booking ---");
        if (b3 != null) {
            facade.cancelBooking(b3.getBookingNumber());
            System.out.println("Seat 12B status after cancellation: " + seat2.getStatus());
            System.out.println("Is Seat 12B available? " + seat2.isAvailable());
        }
    }
}
