package hotelmanagement_sde2;

import hotelmanagement_sde2.payment.Payment;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Replaces the Singleton HotelManagementSystem.
 * Decoupled orchestration layer allowing multiple instances.
 */
public class Hotel {
    private final String hotelName;
    private final Map<String, Guest> guests;
    private final Map<String, Room> rooms;
    private final Map<String, Reservation> globalReservations; // Central lookup table

    public Hotel(String hotelName) {
        this.hotelName = hotelName;
        this.guests = new ConcurrentHashMap<>();
        this.rooms = new ConcurrentHashMap<>();
        this.globalReservations = new ConcurrentHashMap<>();
    }

    public void addGuest(Guest guest) {
        guests.put(guest.getId(), guest);
    }

    public void addRoom(Room room) {
        rooms.put(room.getId(), room);
    }

    public Room getRoom(String roomId) {
        return rooms.get(roomId);
    }

    // Notice the LACK of synchronized here! The core Hotel object does NOT block.
    // The synchronization happens exactly at `room.book()`.
    public Reservation bookRoom(Guest guest, Room room, LocalDate checkInDate, LocalDate checkOutDate) {
        if (checkInDate.isAfter(checkOutDate) || checkInDate.isEqual(checkOutDate)) {
            throw new IllegalArgumentException("Check-out date must be after check-in date.");
        }
        
        // This is perfectly thread-safe at the room-level.
        Reservation reservation = room.book(guest, checkInDate, checkOutDate);
        globalReservations.put(reservation.getId(), reservation);
        return reservation;
    }

    public void cancelReservation(String reservationId) {
        Reservation reservation = globalReservations.get(reservationId);
        if (reservation != null) {
            reservation.cancel(); // Internally thread-safe state transition
        }
    }

    public void checkOut(String reservationId, Payment payment) {
        Reservation reservation = globalReservations.get(reservationId);
        if (reservation != null && reservation.getStatus() == ReservationStatus.CONFIRMED) {
            Room room = reservation.getRoom();
            long days = ChronoUnit.DAYS.between(reservation.getCheckInDate(), reservation.getCheckOutDate());
            BigDecimal amount = BigDecimal.valueOf(room.getPrice() * days);
            
            if (payment.processPayment(amount)) {
                reservation.complete();
            } else {
                throw new IllegalStateException("Payment failed.");
            }
        } else {
            throw new IllegalStateException("Invalid reservation or reservation not active.");
        }
    }
}
