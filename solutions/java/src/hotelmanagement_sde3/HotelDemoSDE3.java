package hotelmanagement_sde3;

public class HotelDemoSDE3 {
    public static void main(String[] args) {
        new BillingService(); // Setup independent processors

        HotelManager manager = new HotelManager();
        manager.addRoom(new Room("101_SUITE", 500.0));

        System.out.println("User U1 books room for 3 nights...");
        manager.reserveRoom("101_SUITE", "U1", 3);
    }
}
