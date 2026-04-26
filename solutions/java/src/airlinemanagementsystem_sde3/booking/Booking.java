package airlinemanagementsystem_sde3.booking;

import airlinemanagementsystem_sde3.flight.Flight;
import airlinemanagementsystem_sde3.Passenger;
import airlinemanagementsystem_sde3.seat.Seat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Booking {
    private final String bookingNumber;
    private final Flight flight;
    private final Passenger passenger;
    private final Seat seat;
    private final BigDecimal price; // SDE2+: Using BigDecimal for currency over double
    private final LocalDateTime creationTime;

    // Volatile as it might be read/written by different threads (e.g. payment callback)
    private volatile BookingStatus status;

    public Booking(String bookingNumber, Flight flight, Passenger passenger, Seat seat, BigDecimal price) {
        this.bookingNumber = bookingNumber;
        this.flight = flight;
        this.passenger = passenger;
        this.seat = seat;
        this.price = price;
        this.creationTime = LocalDateTime.now();
        this.status = BookingStatus.PENDING; 
    }

    public synchronized void confirm() {
        if (this.status == BookingStatus.PENDING) {
            this.status = BookingStatus.CONFIRMED;
        } else {
            throw new IllegalStateException("Booking must be PENDING to confirm");
        }
    }

    public synchronized void cancel() {
        this.status = BookingStatus.CANCELLED;
        // Optionally release the seat as well if coupled, but usually handled by BookingService
        this.seat.release(); 
    }

    public String getBookingNumber() {
        return bookingNumber;
    }

    public Flight getFlight() {
        return flight;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public Seat getSeat() {
        return seat;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }
}
