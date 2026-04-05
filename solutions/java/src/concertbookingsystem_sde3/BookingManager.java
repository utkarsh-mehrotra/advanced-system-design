package concertbookingsystem_sde3;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BookingManager {
    private final Map<String, Seat> seatInventory = new ConcurrentHashMap<>();

    public void addSeat(Seat seat) {
        seatInventory.put(seat.getId(), seat);
    }

    public boolean reserveAndPay(String seatId, String userId) {
        Seat seat = seatInventory.get(seatId);
        if (seat == null) return false;

        // 1. Lock the seat Optimistically
        if (seat.lockSeat()) {
            System.out.println("BookingManager: Seat " + seatId + " locked temporarily for " + userId);
            
            // 2. Perform Payment (Simulated)
            boolean paymentSuccess = simulatePayment();
            
            if (paymentSuccess && seat.confirmBooking()) {
                System.out.println("BookingManager: Payment success. Seat " + seatId + " confirmed.");
                // 3. Emit Async Invoice Event
                String invoiceData = "User:" + userId + ",Seat:" + seatId + ",Amount:" + seat.getPrice();
                EventBus.getInstance().publish("GENERATE_INVOICE", invoiceData);
                return true;
            } else {
                seat.releaseSeat();
                System.out.println("BookingManager: Payment failed. Seat released.");
                return false;
            }
        }
        return false;
    }

    private boolean simulatePayment() {
        return true; // Simplified for demo
    }
}
