package movieticketbookingsystem_upgraded.seat;

public enum SeatStatus {
    AVAILABLE,
    TEMPORARILY_HELD, // SDE3: Critical distributed transaction state preventing racing during payment.
    BOOKED
}
