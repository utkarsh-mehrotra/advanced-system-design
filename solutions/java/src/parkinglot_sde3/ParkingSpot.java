package parkinglot_sde3;

import java.util.concurrent.atomic.AtomicBoolean;

public class ParkingSpot {
    private final String spotId;
    private final AtomicBoolean isOccupied;

    public ParkingSpot(String spotId) {
        this.spotId = spotId;
        this.isOccupied = new AtomicBoolean(false);
    }

    public String getSpotId() { return spotId; }
    public boolean checkOccupied() { return isOccupied.get(); }

    public boolean parkVehicle() {
        return isOccupied.compareAndSet(false, true);
    }

    public void vacate() {
        isOccupied.set(false);
        EventBus.getInstance().publish("SPOT_VACATED", spotId);
    }
}
