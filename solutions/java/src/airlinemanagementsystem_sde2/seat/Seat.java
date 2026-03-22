package airlinemanagementsystem_sde2.seat;

public class Seat {
    private final String seatNumber;
    private final SeatType type;
    
    // Mutable state protected by object monitor
    private SeatStatus status;

    public Seat(String seatNumber, SeatType type) {
        this.seatNumber = seatNumber;
        this.type = type;
        this.status = SeatStatus.AVAILABLE;
    }

    /**
     * Atomically checks and reserves the seat.
     * Prevents Time-of-Check to Time-of-Use (TOCTOU) race conditions.
     * @return true if successfully reserved, false if already reserved/occupied.
     */
    public synchronized boolean reserve() {
        if (this.status == SeatStatus.AVAILABLE) {
            this.status = SeatStatus.RESERVED;
            return true;
        }
        return false;
    }

    public synchronized void release() {
        this.status = SeatStatus.AVAILABLE;
    }

    public synchronized void occupy() {
        if (this.status == SeatStatus.RESERVED) {
            this.status = SeatStatus.OCCUPIED;
        } else {
            throw new IllegalStateException("Seat must be reserved before occupying.");
        }
    }

    public synchronized SeatStatus getStatus() {
        return status;
    }

    public synchronized boolean isAvailable() {
        return status == SeatStatus.AVAILABLE;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public SeatType getType() {
        return type;
    }
}
