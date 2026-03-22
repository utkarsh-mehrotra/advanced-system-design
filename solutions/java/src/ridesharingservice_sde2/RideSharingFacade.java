package ridesharingservice_sde2;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class RideSharingFacade {
    private final Map<String, Ride> rides;
    private final Map<String, Passenger> passengers;
    // CopyOnWriteArrayList: Driver list is read far more often than written
    private final List<Driver> drivers;

    private final PricingStrategy pricingStrategy;
    private final RideDispatcher dispatcher;

    public RideSharingFacade(PricingStrategy pricingStrategy) {
        this.rides = new ConcurrentHashMap<>();
        this.passengers = new ConcurrentHashMap<>();
        this.drivers = new CopyOnWriteArrayList<>();
        this.pricingStrategy = pricingStrategy;
        this.dispatcher = new RideDispatcher();
    }

    public void addPassenger(Passenger p) { passengers.put(p.getId(), p); }
    public void addDriver(Driver d) { drivers.add(d); }

    public Ride requestRide(String passengerId, Location source, Location destination) {
        Passenger passenger = passengers.get(passengerId);
        if (passenger == null) return null;

        String rideId = "RIDE-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Ride ride = new Ride(rideId, passenger, source, destination);
        rides.put(rideId, ride);

        // Fire-and-forget: main thread returns immediately, broadcast happens asynchronously
        dispatcher.broadcastToNearbyDrivers(ride, drivers);
        System.out.println("Ride " + rideId + " placed. Driver notification dispatched asynchronously.");
        return ride;
    }

    /**
     * SDE3: Called when a driver taps "Accept". If 3 drivers tap simultaneously,
     * the per-Ride lock + Driver CAS ensures exactly ONE succeeds.
     */
    public boolean acceptRide(String rideId, String driverId) {
        Ride ride = rides.get(rideId);
        Driver driver = drivers.stream().filter(d -> d.getId().equals(driverId)).findFirst().orElse(null);

        if (ride == null || driver == null) return false;

        boolean accepted = ride.assignDriverAtomically(driver);
        if (accepted) {
            System.out.println("Driver " + driver.getName() + " exclusively accepted Ride " + rideId);
        } else {
            System.out.println("Driver " + driver.getName() + " was REJECTED — Ride " + rideId + " already claimed!");
        }
        return accepted;
    }

    public void startRide(String rideId) {
        Ride ride = rides.get(rideId);
        if (ride != null && ride.startRide()) {
            System.out.println("Ride " + rideId + " started.");
        }
    }

    public BigDecimal completeRide(String rideId) {
        Ride ride = rides.get(rideId);
        if (ride == null) return BigDecimal.ZERO;

        double distanceKm = ride.getSource().distanceTo(ride.getDestination());
        double durationMin = (distanceKm / 30.0) * 60; // Assume 30 km/h average speed

        BigDecimal fare = pricingStrategy.calculateFare(distanceKm, durationMin);
        if (ride.completeRide(fare)) {
            System.out.printf("Ride %s completed. Fare: $%s%n", rideId, fare.toPlainString());
        }
        return fare;
    }

    public void shutdown() {
        dispatcher.shutdown();
    }
}
