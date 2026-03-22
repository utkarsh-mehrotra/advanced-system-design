package movieticketbookingsystem_sde2.seat;

public enum SeatStatus {
    AVAILABLE,
    TEMPORARILY_HELD, // SDE3: Critical distributed transaction state preventing racing during payment.
    BOOKED
}
