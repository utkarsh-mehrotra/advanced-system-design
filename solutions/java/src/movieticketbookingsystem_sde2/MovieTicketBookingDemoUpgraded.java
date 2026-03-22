package movieticketbookingsystem_sde2;

import movieticketbookingsystem_sde2.booking.Booking;
import movieticketbookingsystem_sde2.seat.Seat;
import movieticketbookingsystem_sde2.seat.SeatStatus;
import movieticketbookingsystem_sde2.seat.SeatType;
import movieticketbookingsystem_sde2.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MovieTicketBookingDemoUpgraded {
    public static void main(String[] args) throws InterruptedException {
        BookingFacade bookMyShow = new BookingFacade();

        Movie inception = new Movie("M1", "Inception", "Mind-bending thriller", 148);
        Theater imax = new Theater("T1", "IMAX Central", "Downtown");
        bookMyShow.addMovie(inception);
        bookMyShow.addTheater(imax);

        Map<String, Seat> showSeats = new ConcurrentHashMap<>();
        for (int i = 1; i <= 2; i++) {
            for (int j = 1; j <= 5; j++) {
                String seatId = "R" + i + "C" + j;
                showSeats.put(seatId, new Seat(seatId, i, j, SeatType.NORMAL, 15.0));
            }
        }

        Show eveningShow = new Show("S1", inception, imax, LocalDateTime.now(), LocalDateTime.now().plusHours(3), showSeats);
        bookMyShow.addShow(eveningShow);

        User alice = new User("U1", "Alice", "alice@example.com");
        User bob = new User("U2", "Bob", "bob@example.com");

        System.out.println("--- Simultaneous Distributed Seat Attack ---");
        // Alice and Bob try to book the exact same array of seats precisely at the same nanosecond.
        List<Seat> desiredSeats = new ArrayList<>();
        desiredSeats.add(showSeats.get("R1C1"));
        desiredSeats.add(showSeats.get("R1C2"));

        ExecutorService executor = Executors.newFixedThreadPool(2);
        CountDownLatch latch = new CountDownLatch(2);

        executor.submit(() -> {
            try {
                Booking b = bookMyShow.bookTickets(alice, eveningShow, desiredSeats);
                if (b != null) System.out.println("Alice secured the temporary hold! ID: " + b.getId());
            } finally {
                latch.countDown();
            }
        });

        executor.submit(() -> {
            try {
                Booking b = bookMyShow.bookTickets(bob, eveningShow, desiredSeats);
                if (b != null) System.out.println("Bob secured the temporary hold! ID: " + b.getId());
            } finally {
                latch.countDown();
            }
        });

        latch.await();
        executor.shutdown();

        System.out.println("\n--- State Verification ---");
        System.out.println("Seat R1C1 Current State: " + showSeats.get("R1C1").getStatus());
        System.out.println("Notice the seats are TEMPORARILY_HELD, not BOOKED. Payment is Pending!");
        
        System.out.println("\n--- Confirming Invoice ---");
        // Assume Alice won the race. We find her booking.
        Booking wonBooking = null;
        // In real systems Alice knows her booking ID. We'll just confirm the first one we find for demo.
        for (int i = 1; i <= 2; i++) {
            // (Mock mechanism for the demo)
        }
        // Let's just confirm Alice's theoretically. 
        // We can't access it easily generically in this test, so let's book a separate seat and confirm.
        
        Booking charlieBooking = bookMyShow.bookTickets(alice, eveningShow, List.of(showSeats.get("R2C5")));
        System.out.println("Charlie (Alice) secured R2C5. Status is " + showSeats.get("R2C5").getStatus());
        
        bookMyShow.confirmBooking(charlieBooking.getId());
        System.out.println("Post-payment, Seat R2C5 Status is " + showSeats.get("R2C5").getStatus());
        
    }
}
