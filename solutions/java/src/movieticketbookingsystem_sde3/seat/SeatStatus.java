package movieticketbookingsystem_sde3.seat;

public enum SeatStatus {
    AVAILABLE,
    TEMPORARILY_HELD, // SDE3: Critical distributed transaction state preventing racing during payment.
    BOOKED
}
