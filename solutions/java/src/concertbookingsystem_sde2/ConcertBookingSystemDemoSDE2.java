package concertbookingsystem_sde2;

public class ConcertBookingSystemDemoSDE2 {
    public static void main(String[] args) {
        TicketManager manager = new TicketManager("COLDPLAY_MUMBAI");
        manager.addObserver(new NotificationService());

        manager.addSeat(new Seat("S1", "A1", 5000.0));
        manager.addSeat(new Seat("S2", "A2", 5000.0));

        System.out.println("Attempting to book S1...");
        boolean success = manager.bookSeat("S1");
        System.out.println("Booking success: " + success);

        System.out.println("Releasing S1...");
        manager.releaseSeat("S1");
    }
}
