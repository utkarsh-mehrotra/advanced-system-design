package movieticketbookingsystem_sde3;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BookingManager {
    private final Map<String, Seat> hallLayout = new ConcurrentHashMap<>();

    public void initializeSeat(Seat seat) {
        hallLayout.put(seat.getSeatId(), seat);
    }

    public void processBooking(String userId, String seatId) {
        Seat seat = hallLayout.get(seatId);
        if (seat != null) {
            if (seat.lockForPayment()) {
                System.out.println("BookingManager: Seat " + seatId + " locked for user " + userId);
                
                // Simulate Payment Success
                if (seat.confirmPayment()) {
                    EventBus.getInstance().publish("TICKET_CONFIRMED", userId + ":" + seatId);
                } else {
                    seat.rollbackLock();
                }
            } else {
                System.out.println("BookingManager: Seat " + seatId + " is already taken!");
            }
        }
    }
}
