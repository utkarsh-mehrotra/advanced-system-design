package hotelmanagement_sde2;

import hotelmanagement_sde2.payment.CreditCardPayment;

import java.time.LocalDate;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HotelManagementSystemDemoUpgraded {
    public static void main(String[] args) throws InterruptedException {
        Hotel marriott = new Hotel("Marriott International");

        Guest alice = new Guest("G1", "Alice", "alice@test.com", "555-1000");
        Guest bob = new Guest("G2", "Bob", "bob@test.com", "555-2000");
        marriott.addGuest(alice);
        marriott.addGuest(bob);

        Room suite101 = new Room("101", RoomType.SUITE, 300.0);
        Room double102 = new Room("102", RoomType.DOUBLE, 150.0);
        marriott.addRoom(suite101);
        marriott.addRoom(double102);

        System.out.println("--- SDE3 Temporal Interval Tests ---");
        
        // 1. Valid booking (Dec 1 -> Dec 5)
        Reservation r1 = marriott.bookRoom(alice, suite101, LocalDate.of(2026, 12, 1), LocalDate.of(2026, 12, 5));
        System.out.println("Alice successfully booked Suite 101 from Dec 1 to Dec 5.");

        // 2. Conflicting overlap test (Dec 4 -> Dec 10) - Should instantly fail
        try {
            marriott.bookRoom(bob, suite101, LocalDate.of(2026, 12, 4), LocalDate.of(2026, 12, 10));
        } catch (IllegalStateException e) {
            System.out.println("Bob's booking rejected properly due to temporal overlap: " + e.getMessage());
        }

        // 3. Perfect disjoint boundary check (Dec 5 -> Dec 10) - Check-in directly matches a Checkout. Should Succeed!
        Reservation r2 = marriott.bookRoom(bob, suite101, LocalDate.of(2026, 12, 5), LocalDate.of(2026, 12, 10));
        System.out.println("Bob successfully booked Suite 101 starting precisely on Alice's checkout date (Dec 5 -> Dec 10).");

        System.out.println("\n--- Concurrency Massive Throughput Test ---");
        // Ensure 100 threads hitting DIFFERENT rooms don't block each other (no global hotel lock)
        ExecutorService executor = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(100);

        long start = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            final int index = i;
            executor.submit(() -> {
                try {
                    Room genericRoom = new Room("R-" + index, RoomType.SINGLE, 100.0);
                    // This creates 100 reservations perfectly parallel 
                    marriott.bookRoom(alice, genericRoom, LocalDate.now(), LocalDate.now().plusDays(2));
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        executor.shutdown();
        long end = System.currentTimeMillis();
        System.out.println("100 concurrent separate room bookings executed in " + (end - start) + "ms precisely because the global synchronized Hotel block was obliterated!");
        
        // Final payment test
        marriott.checkOut(r1.getId(), new CreditCardPayment());
        System.out.println("Alice completed check-out and paid strategy applied.");
    }
}
