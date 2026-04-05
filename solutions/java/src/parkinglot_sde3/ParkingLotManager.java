package parkinglot_sde3;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ParkingLotManager {
    private final Map<String, ParkingSpot> grid = new ConcurrentHashMap<>();
    private final AtomicInteger availableCapacity;

    public ParkingLotManager(int totalCapacity) {
        this.availableCapacity = new AtomicInteger(totalCapacity);
    }

    public void registerSpot(ParkingSpot spot) {
        grid.put(spot.getSpotId(), spot);
    }

    public String attemptAssignment() {
        while (true) {
            int current = availableCapacity.get();
            if (current <= 0) return null;
            
            if (availableCapacity.compareAndSet(current, current - 1)) {
                // Find first free spot
                for (ParkingSpot spot : grid.values()) {
                    if (spot.parkVehicle()) { // Lock-free exact check
                        EventBus.getInstance().publish("TICKET_ISSUED", spot.getSpotId());
                        return spot.getSpotId();
                    }
                }
                // Rollback if mapping skew occurs
                availableCapacity.incrementAndGet();
                return null;
            }
        }
    }
}
