package hotelmanagement_sde3;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Room {
    private final String id;
    private final RoomType type;
    private final double price;
    
    // SDE3: Temporal booking ledger rather than a single 'BOOKED' / 'AVAILABLE' flag.
    private final List<Reservation> reservations;
    
    // SDE3: Fine-grained concurrency. 
    // Millions of users can book different rooms inside the Hotel. The lock only contends AT the physical room level.
    private final Lock lock;

    public Room(String id, RoomType type, double price) {
        this.id = id;
        this.type = type;
        this.price = price;
        this.reservations = new ArrayList<>();
        this.lock = new ReentrantLock();
    }

    /**
     * Executes the temporal validation ensuring the new [checkIn, checkOut] request 
     * mathematically disjoints from all existing active bindings for this room.
     */
    public Reservation book(Guest guest, LocalDate checkIn, LocalDate checkOut) {
        lock.lock();
        try {
            for (Reservation res : reservations) {
                if (res.getStatus() == ReservationStatus.CONFIRMED) {
                    // Standard interval overlap math: startA < endB && endA > startB
                    // Note: checkIn < existingOut AND checkOut > existingIn means overlap!
                    if (checkIn.isBefore(res.getCheckOutDate()) && checkOut.isAfter(res.getCheckInDate())) {
                        throw new IllegalStateException("Room " + id + " is already booked during this interval.");
                    }
                }
            }
            
            String reservationId = "RES-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            Reservation newRes = new Reservation(reservationId, guest, this, checkIn, checkOut);
            reservations.add(newRes);
            return newRes;
            
        } finally {
            lock.unlock();
        }
    }

    public String getId() { return id; }
    public RoomType getType() { return type; }
    public double getPrice() { return price; }
    // Thread safe clone for external viewers
    public List<Reservation> getReservations() {
        lock.lock();
        try {
            return new ArrayList<>(reservations);
        } finally {
            lock.unlock();
        }
    }
}
