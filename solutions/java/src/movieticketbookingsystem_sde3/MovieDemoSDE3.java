package movieticketbookingsystem_sde3;

public class MovieDemoSDE3 {
    public static void main(String[] args) {
        new TicketService(); 

        BookingManager manager = new BookingManager();
        manager.initializeSeat(new Seat("ROW_A_1", 300.0));

        System.out.println("User attempts to book ROW_A_1...");
        manager.processBooking("U_1", "ROW_A_1");
        
        System.out.println("User 2 attempts concurrently...");
        manager.processBooking("U_2", "ROW_A_1"); // Should be rejected instantly
    }
}
