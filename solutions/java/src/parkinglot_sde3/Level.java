package parkinglot_sde3;

import parkinglot_sde3.vehicletype.VehicleType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Level {
    private final int floor;
    private final List<ParkingSpot> parkingSpots;

    public Level(int floor, int numSpots) {
        this.floor = floor;
        List<ParkingSpot> tempSpots = new ArrayList<>(numSpots);
        
        double spotsForBikes = 0.50;
        double spotsForCars = 0.40;

        int numBikes = (int) (numSpots * spotsForBikes);
        int numCars = (int) (numSpots * spotsForCars);

        for (int i = 1; i <= numBikes; i++) {
            tempSpots.add(new ParkingSpot(i, VehicleType.MOTORCYCLE));
        }
        for (int i = numBikes + 1; i <= numBikes + numCars; i++) {
            tempSpots.add(new ParkingSpot(i, VehicleType.CAR));
        }
        for (int i = numBikes + numCars + 1; i <= numSpots; i++) {
            tempSpots.add(new ParkingSpot(i, VehicleType.TRUCK));
        }
        
        // Expose as unmodifiable list. Internal spots are thread safe themselves via Atomic primitives.
        this.parkingSpots = Collections.unmodifiableList(tempSpots);
    }

    public int getFloor() {
        return floor;
    }

    public List<ParkingSpot> getParkingSpots() {
        return parkingSpots;
    }

    public void displayAvailability() {
        System.out.println("Level " + floor + " Availability:");
        for (ParkingSpot spot : parkingSpots) {
            String status = spot.isAvailable() ? "Available For " : ("Occupied by " + spot.getParkedLicensePlate() + " (For ");
            System.out.println("Spot " + spot.getSpotNumber() + ": " + status + spot.getVehicleType() + ")");
        }
    }
}
