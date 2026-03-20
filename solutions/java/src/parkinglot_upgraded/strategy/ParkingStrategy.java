package parkinglot_upgraded.strategy;

import parkinglot_upgraded.Level;
import parkinglot_upgraded.ParkingSpot;
import parkinglot_upgraded.vehicletype.VehicleType;

import java.util.List;
import java.util.Optional;

public interface ParkingStrategy {
    /**
     * Finds and reserves the next available spot for the given vehicle type.
     * @param levels The levels in the parking lot
     * @param vehicleType The type of vehicle to park
     * @param licensePlate The license plate of the vehicle
     * @return An Optional containing the reserved ParkingSpot, or empty if lot is full.
     */
    Optional<ParkingSpot> findAndReserveSpot(List<Level> levels, VehicleType vehicleType, String licensePlate);
}
