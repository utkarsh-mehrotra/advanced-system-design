package airlinemanagementsystem_sde3.booking;

import airlinemanagementsystem_sde3.Passenger;
import airlinemanagementsystem_sde3.flight.Flight;
import airlinemanagementsystem_sde3.seat.Seat;

import java.math.BigDecimal;
import java.util.List;

public interface BookingService {
    Booking createBooking(Flight flight, Passenger passenger, Seat seat, BigDecimal price);
    void cancelBooking(String bookingNumber);
    Booking getBooking(String bookingNumber);
    List<Booking> getAllBookings();
}
