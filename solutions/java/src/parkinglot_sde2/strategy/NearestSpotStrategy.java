package parkinglot_sde2.strategy;

import parkinglot_sde2.Level;
import parkinglot_sde2.ParkingSpot;
import parkinglot_sde2.vehicletype.VehicleType;

import java.util.List;
import java.util.Optional;

public class NearestSpotStrategy implements ParkingStrategy {

    @Override
    public Optional<ParkingSpot> findAndReserveSpot(List<Level> levels, VehicleType vehicleType, String licensePlate) {
        // Iterate through levels (assumed ordered by proximity to entrance)
        for (Level level : levels) {
            for (ParkingSpot spot : level.getParkingSpots()) {
                // If it looks available and matches type, try to reserve it atomically
                if (spot.getVehicleType() == vehicleType && spot.isAvailable()) {
                    boolean reserved = spot.park(licensePlate);
                    if (reserved) {
                        return Optional.of(spot); // Successfully reserved
                    }
                    // If reserved == false, another thread grabbed it in between `isAvailable()` and `park()`.
                    // We just continue searching the next spot. Lock-free concurrency!
                }
            }
        }
        return Optional.empty(); // Lot is full for this vehicle type
    }
}
