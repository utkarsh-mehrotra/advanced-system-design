package airlinemanagementsystem_upgraded.booking;

import airlinemanagementsystem_upgraded.Passenger;
import airlinemanagementsystem_upgraded.flight.Flight;
import airlinemanagementsystem_upgraded.seat.Seat;

import java.math.BigDecimal;
import java.util.List;

public interface BookingService {
    Booking createBooking(Flight flight, Passenger passenger, Seat seat, BigDecimal price);
    void cancelBooking(String bookingNumber);
    Booking getBooking(String bookingNumber);
    List<Booking> getAllBookings();
}
