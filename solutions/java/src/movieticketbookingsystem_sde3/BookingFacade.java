package movieticketbookingsystem_sde3;

import movieticketbookingsystem_sde3.booking.Booking;
import movieticketbookingsystem_sde3.booking.BookingStatus;
import movieticketbookingsystem_sde3.seat.Seat;
import movieticketbookingsystem_sde3.user.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * An SDE3/FAANG Instantiatable backend orchestration facade.
 * Distinctly DOES NOT USE a synchronized block on its core `bookTickets` API.
 */
public class BookingFacade {
    private final List<Movie> movies;
    private final List<Theater> theaters;
    private final Map<String, Show> shows;
    private final Map<String, Booking> bookings;

    private static final String BOOKING_ID_PREFIX = "BKG";
    private static final AtomicLong bookingCounter = new AtomicLong(0);

    public BookingFacade() {
        movies = new ArrayList<>();
        theaters = new ArrayList<>();
        shows = new ConcurrentHashMap<>();
        bookings = new ConcurrentHashMap<>();
    }

    public void addMovie(Movie movie) { movies.add(movie); }
    public void addTheater(Theater theater) { theaters.add(theater); }
    public void addShow(Show show) { shows.put(show.getId(), show); }
    public Show getShow(String showId) { return shows.get(showId); }

    /**
     * SDE3 Lock-Free Facade Router. 
     * Concurrency is delegated EXCLUSIVELY to the targeted Show!
     */
    public Booking bookTickets(User user, Show show, List<Seat> selectedSeats) {
        // Atomic transaction hold across the specific room.
        boolean secured = show.lockAndHoldSeats(selectedSeats);
        
        if (secured) {
            double totalPrice = selectedSeats.stream().mapToDouble(Seat::getPrice).sum();
            String bookingId = generateBookingId();
            Booking booking = new Booking(bookingId, user, show, selectedSeats, totalPrice, BookingStatus.PENDING);
            bookings.put(bookingId, booking);
            return booking;
        }
        
        System.out.println("Could not secure seats. They are either booked or currently temporally held by another shopper.");
        return null;
    }

    public void confirmBooking(String bookingId) {
        Booking booking = bookings.get(bookingId);
        if (booking != null && booking.getStatus() == BookingStatus.PENDING) {
            booking.setStatus(BookingStatus.CONFIRMED);
            // SDE3: Transition seats from TEMPORARILY_HELD -> BOOKED
            booking.getShow().finalizeSeats(booking.getSeats(), true);
            System.out.println("Payment Success. Booking " + bookingId + " Confirmed.");
        }
    }

    public void cancelBooking(String bookingId) {
        Booking booking = bookings.get(bookingId);
        if (booking != null && booking.getStatus() != BookingStatus.CANCELLED) {
            booking.setStatus(BookingStatus.CANCELLED);
            // SDE3: Transition seats from BOOKED/TEMPORARILY_HELD -> AVAILABLE
            booking.getShow().finalizeSeats(booking.getSeats(), false);
            System.out.println("Booking " + bookingId + " Cancelled. Seats released.");
        }
    }

    private String generateBookingId() {
        long bookingNumber = bookingCounter.incrementAndGet();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return BOOKING_ID_PREFIX + timestamp + String.format("%06d", bookingNumber);
    }
}
