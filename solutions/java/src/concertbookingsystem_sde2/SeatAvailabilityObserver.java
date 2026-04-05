package concertbookingsystem_sde2;

public interface SeatAvailabilityObserver {
    void onSeatAvailable(String concertId, String seatId);
    void onSeatBooked(String concertId, String seatId);
}

class NotificationService implements SeatAvailabilityObserver {
    @Override
    public void onSeatAvailable(String concertId, String seatId) {
        System.out.println("Notification: Seat " + seatId + " for concert " + concertId + " is now AVAILABLE.");
    }

    @Override
    public void onSeatBooked(String concertId, String seatId) {
        System.out.println("Notification: Seat " + seatId + " for concert " + concertId + " has been BOOKED.");
    }
}
