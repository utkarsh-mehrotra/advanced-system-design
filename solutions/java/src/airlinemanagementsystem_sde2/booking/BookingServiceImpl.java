package airlinemanagementsystem_sde2.booking;

import airlinemanagementsystem_sde2.Passenger;
import airlinemanagementsystem_sde2.exception.SeatAlreadyBookedException;
import airlinemanagementsystem_sde2.flight.Flight;
import airlinemanagementsystem_sde2.seat.Seat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class BookingServiceImpl implements BookingService {
    
    // SDE2+: Uses ConcurrentHashMap instead of global lock synchronization for multi-theaded insertion.
    private final Map<String, Booking> bookings;
    private final AtomicInteger bookingCounter;

    public BookingServiceImpl() {
        this.bookings = new ConcurrentHashMap<>();
        this.bookingCounter = new AtomicInteger(0);
    }

    @Override
    public Booking createBooking(Flight flight, Passenger passenger, Seat seat, BigDecimal price) {
        // Atomic Seat Reservation. Eliminates Time-of-Check to Time-of-Use race condition.
        boolean reserved = seat.reserve(); 
        
        if (!reserved) {
            throw new SeatAlreadyBookedException("Seat " + seat.getSeatNumber() + " is already booked or occupied.");
        }

        String bookingNumber = generateBookingNumber();
        Booking booking = new Booking(bookingNumber, flight, passenger, seat, price);

        // Map handles thread-safe placement without blocking other bookings
        bookings.put(bookingNumber, booking);
        
        System.out.println("Booking created: " + bookingNumber + ". Status: " + booking.getStatus());
        return booking;
    }

    @Override
    public void cancelBooking(String bookingNumber) {
        Booking booking = bookings.get(bookingNumber);
        if (booking != null) {
            booking.cancel(); // Internally handled synchronously and seat released
            System.out.println("Booking cancelled: " + bookingNumber);
        } else {
            System.err.println("Cancel failed: Booking not found.");
        }
    }

    @Override
    public Booking getBooking(String bookingNumber) {
        return bookings.get(bookingNumber);
    }
    
    @Override
    public List<Booking> getAllBookings() {
        return new ArrayList<>(bookings.values());
    }

    private String generateBookingNumber() {
        int bookingId = bookingCounter.incrementAndGet();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return "BKG" + timestamp + String.format("%06d", bookingId);
    }
}
