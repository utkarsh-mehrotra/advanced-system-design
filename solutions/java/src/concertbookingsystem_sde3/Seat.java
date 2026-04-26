package concertbookingsystem_sde3;

public class Seat {
    private final String id;
    private final String seatNumber;
    private final double price;
    private boolean isAvailable;

    public Seat(String id, String seatNumber, double price) {
        this.id = id;
        this.seatNumber = seatNumber;
        this.price = price;
        this.isAvailable = true;
    }

    public String getId() { return id; }
    public String getSeatNumber() { return seatNumber; }
    public double getPrice() { return price; }
    public boolean isAvailable() { return isAvailable; }

    void setAvailable(boolean available) {
        this.isAvailable = available;
    }
}
