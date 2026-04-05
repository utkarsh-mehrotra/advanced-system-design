package concertbookingsystem_sde3;

import java.util.concurrent.atomic.AtomicReference;

public class Seat {
    private final String id;
    private final double price;
    private final AtomicReference<SeatStatus> state;

    public Seat(String id, double price) {
        this.id = id;
        this.price = price;
        this.state = new AtomicReference<>(SeatStatus.AVAILABLE);
    }

    public String getId() { return id; }
    public double getPrice() { return price; }
    
    // Core CAS lock logic!
    public boolean lockSeat() {
        return state.compareAndSet(SeatStatus.AVAILABLE, SeatStatus.LOCKED);
    }
    
    public boolean confirmBooking() {
        return state.compareAndSet(SeatStatus.LOCKED, SeatStatus.BOOKED);
    }
    
    public boolean releaseSeat() {
        return state.compareAndSet(SeatStatus.LOCKED, SeatStatus.AVAILABLE);
    }
    
    public SeatStatus getStatus() {
        return state.get();
    }
}
