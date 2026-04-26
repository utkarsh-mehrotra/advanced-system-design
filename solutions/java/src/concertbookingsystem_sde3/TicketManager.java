package concertbookingsystem_sde3;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TicketManager {
    // Thread-safe map of seats
    private final Map<String, Seat> seatInventory = new ConcurrentHashMap<>();
    private final ReentrantLock lock = new ReentrantLock();
    
    // Observers
    private final List<SeatAvailabilityObserver> observers = new CopyOnWriteArrayList<>();
    private final String concertId;

    public TicketManager(String concertId) {
        this.concertId = concertId;
    }

    public void addObserver(SeatAvailabilityObserver observer) {
        observers.add(observer);
    }

    public void addSeat(Seat seat) {
        seatInventory.put(seat.getId(), seat);
    }

    public boolean bookSeat(String seatId) {
        lock.lock();
        try {
            Seat seat = seatInventory.get(seatId);
            if (seat != null && seat.isAvailable()) {
                seat.setAvailable(false);
                notifyObserversBooked(seatId);
                return true;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

    public void releaseSeat(String seatId) {
        lock.lock();
        try {
            Seat seat = seatInventory.get(seatId);
            if (seat != null && !seat.isAvailable()) {
                seat.setAvailable(true);
                notifyObserversAvailable(seatId);
            }
        } finally {
            lock.unlock();
        }
    }

    private void notifyObserversBooked(String seatId) {
        for (SeatAvailabilityObserver obs : observers) {
            obs.onSeatBooked(concertId, seatId);
        }
    }

    private void notifyObserversAvailable(String seatId) {
        for (SeatAvailabilityObserver obs : observers) {
            obs.onSeatAvailable(concertId, seatId);
        }
    }
}
