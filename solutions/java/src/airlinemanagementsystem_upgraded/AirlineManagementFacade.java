package airlinemanagementsystem_upgraded;

import airlinemanagementsystem_upgraded.booking.Booking;
import airlinemanagementsystem_upgraded.booking.BookingService;
import airlinemanagementsystem_upgraded.exception.PaymentFailedException;
import airlinemanagementsystem_upgraded.flight.Flight;
import airlinemanagementsystem_upgraded.flight.FlightSearchService;
import airlinemanagementsystem_upgraded.payment.Payment;
import airlinemanagementsystem_upgraded.payment.PaymentService;
import airlinemanagementsystem_upgraded.payment.PaymentStatus;
import airlinemanagementsystem_upgraded.seat.Seat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Acts as a unified API for clients, orchestrating interactions between 
 * domain services (Booking, Payment, Flight Search).
 * Fully utilizes constructor injection (DI) to decouple implementation details.
 */
public class AirlineManagementFacade {
    private final FlightSearchService flightSearchService;
    private final BookingService bookingService;
    private final PaymentService paymentService;

    // Use Dependency Injection for testability and avoiding rigid singletons
    public AirlineManagementFacade(FlightSearchService flightSearchService, 
                                   BookingService bookingService, 
                                   PaymentService paymentService) {
        this.flightSearchService = flightSearchService;
        this.bookingService = bookingService;
        this.paymentService = paymentService;
    }

    public List<Flight> searchFlights(String source, String destination, LocalDate date) {
        return flightSearchService.searchFlights(source, destination, date);
    }

    /**
     * Orchestrates the complex sequence:
     * 1. Create PENDING booking (reserves seat atomically).
     * 2. Attempt Payment.
     * 3. Confirm booking if payment succeeds, or rollback/cancel if it fails.
     */
    public Booking bookFlight(Flight flight, Passenger passenger, Seat seat, BigDecimal price) {
        // 1. Reserve Seat and Create Pending Booking
        // This validates seat availability concurrently inside the service and locks it briefly.
        Booking booking = bookingService.createBooking(flight, passenger, seat, price);

        // 2. Transact Payment
        Payment payment = new Payment(UUID.randomUUID().toString(), booking.getBookingNumber(), price);
        boolean isSuccess = paymentService.processPayment(payment);

        // 3. Confirm or Rollback
        if (isSuccess && payment.getStatus() == PaymentStatus.COMPLETED) {
            booking.confirm();
            System.out.println("Booking " + booking.getBookingNumber() + " successfully CONFIRMED.");
            return booking;
        } else {
            System.err.println("Booking " + booking.getBookingNumber() + " failed due to payment failure.");
            bookingService.cancelBooking(booking.getBookingNumber()); // Rolling back reservation
            throw new PaymentFailedException("Payment declined or interrupted for Booking: " + booking.getBookingNumber());
        }
    }

    public void cancelBooking(String bookingNumber) {
        bookingService.cancelBooking(bookingNumber);
    }
}
