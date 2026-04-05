package concertbookingsystem_sde3;

public class ConcertBookingSystemDemoSDE3 {
    public static void main(String[] args) {
        // Initialize independent background services
        new InvoiceService();

        BookingManager manager = new BookingManager();
        manager.addSeat(new Seat("FRONT_ROW_1", 10000.0));

        System.out.println("User U1 attempting to book...");
        manager.reserveAndPay("FRONT_ROW_1", "U1");
        
        System.out.println("User U2 attempting to book same seat...");
        boolean success = manager.reserveAndPay("FRONT_ROW_1", "U2");
        System.out.println("User U2 success: " + success);
    }
}
