package movieticketbookingsystem_sde3;

import java.util.concurrent.atomic.AtomicReference;

public class Seat {
    private final String seatId;
    private final double price;
    private final AtomicReference<SeatStatus> status;

    public Seat(String seatId, double price) {
        this.seatId = seatId;
        this.price = price;
        this.status = new AtomicReference<>(SeatStatus.AVAILABLE);
    }

    public String getSeatId() { return seatId; }
    public double getPrice() { return price; }

    public boolean lockForPayment() {
        return status.compareAndSet(SeatStatus.AVAILABLE, SeatStatus.LOCKED_FOR_PAYMENT);
    }

    public boolean confirmPayment() {
        return status.compareAndSet(SeatStatus.LOCKED_FOR_PAYMENT, SeatStatus.BOOKED);
    }

    public boolean rollbackLock() {
        return status.compareAndSet(SeatStatus.LOCKED_FOR_PAYMENT, SeatStatus.AVAILABLE);
    }
}
