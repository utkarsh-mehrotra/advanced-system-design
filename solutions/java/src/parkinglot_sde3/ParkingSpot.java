package parkinglot_sde3;

import parkinglot_sde3.vehicletype.VehicleType;

import java.util.concurrent.atomic.AtomicBoolean;

public class ParkingSpot {
    private final int spotNumber;
    private final VehicleType vehicleType;
    private String parkedLicensePlate; // No need to store whole Vehicle logic entity if parked

    // SDE2+: Using AtomicBoolean for lock-free thread safety
    private final AtomicBoolean isAvailable;

    public ParkingSpot(int spotNumber, VehicleType vehicleType) {
        this.spotNumber = spotNumber;
        this.vehicleType = vehicleType;
        this.isAvailable = new AtomicBoolean(true);
    }

    /**
     * Attempts to park atomically.
     * @param licensePlate plate of the vehicle
     * @return true if successful reservation, false if already occupied
     */
    public boolean park(String licensePlate) {
        // Atomic compare-and-swap from true to false
        if (isAvailable.compareAndSet(true, false)) {
            this.parkedLicensePlate = licensePlate;
            return true;
        }
        return false;
    }

    /**
     * Atomic release
     */
    public void unpark() {
        this.parkedLicensePlate = null;
        isAvailable.set(true); // Release the spot atomically
    }

    public boolean isAvailable() {
        return isAvailable.get();
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public int getSpotNumber() {
        return spotNumber;
    }

    public String getParkedLicensePlate() {
        return parkedLicensePlate;
    }
}
