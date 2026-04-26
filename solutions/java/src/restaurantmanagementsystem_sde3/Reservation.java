package restaurantmanagementsystem_sde3;

import java.time.LocalDateTime;

public class Reservation {
    private final int id;
    private final String customerName;
    private final int partySize;
    private final LocalDateTime reservationTime;

    public Reservation(int id, String customerName, int partySize, LocalDateTime reservationTime) {
        this.id = id;
        this.customerName = customerName;
        this.partySize = partySize;
        this.reservationTime = reservationTime;
    }

    public int getId() { return id; }
    public String getCustomerName() { return customerName; }
    public int getPartySize() { return partySize; }
    public LocalDateTime getReservationTime() { return reservationTime; }
}
