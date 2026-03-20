package movieticketbookingsystem_upgraded;

import movieticketbookingsystem_upgraded.seat.Seat;
import movieticketbookingsystem_upgraded.seat.SeatStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Show {
    private final String id;
    private final Movie movie;
    private final Theater theater;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final Map<String, Seat> seats;
    
    // SDE3: Granular Lock mechanism!
    // Replaces the `MovieTicketBookingSystem` global synchronized monolith.
    // Booking a seat at the "Avengers" Show only locks the "Avengers" Show, not the entire platform.
    private final Lock showLock;

    public Show(String id, Movie movie, Theater theater, LocalDateTime startTime, LocalDateTime endTime, Map<String, Seat> seats) {
        this.id = id;
        this.movie = movie;
        this.theater = theater;
        this.startTime = startTime;
        this.endTime = endTime;
        this.seats = seats;
        this.showLock = new ReentrantLock();
    }

    /**
     * Atomically grabs the seats and flips them to TEMPORARILY_HELD so no one else can snatch them
     * while the user's credit card processes.
     */
    public boolean lockAndHoldSeats(List<Seat> selectedSeats) {
        showLock.lock();
        try {
            // Transactional Verify
            for (Seat seat : selectedSeats) {
                if (this.seats.get(seat.getId()).getStatus() != SeatStatus.AVAILABLE) {
                    return false; // Transaction fail, cannot secure all seats
                }
            }
            
            // Transactional Commit
            for (Seat seat : selectedSeats) {
                this.seats.get(seat.getId()).setStatus(SeatStatus.TEMPORARILY_HELD);
            }
            return true;
        } finally {
            showLock.unlock();
        }
    }

    /**
     * Completes or cancels a hold. 
     */
    public void finalizeSeats(List<Seat> selectedSeats, boolean confirmedPayment) {
        showLock.lock();
        try {
            SeatStatus newStatus = confirmedPayment ? SeatStatus.BOOKED : SeatStatus.AVAILABLE;
            for (Seat seat : selectedSeats) {
                this.seats.get(seat.getId()).setStatus(newStatus);
            }
        } finally {
            showLock.unlock();
        }
    }

    public String getId() { return id; }
    public Movie getMovie() { return movie; }
    public Theater getTheater() { return theater; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public Map<String, Seat> getSeats() { return seats; }
}
