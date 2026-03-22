package ridesharingservice_sde2;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * SDE3: Asynchronous proximity-based driver notification dispatcher.
 * Prevents the request thread from blocking on O(N) distance sweeps.
 */
public class RideDispatcher {
    private static final double PROXIMITY_RADIUS_KM = 5.0;
    private final ExecutorService dispatchPool;

    public RideDispatcher() {
        this.dispatchPool = Executors.newFixedThreadPool(8);
    }

    /**
     * Asynchronously broadcasts a ride request to nearby available drivers.
     */
    public void broadcastToNearbyDrivers(Ride ride, List<Driver> allDrivers) {
        dispatchPool.submit(() -> {
            for (Driver driver : allDrivers) {
                if (driver.isAvailable()) {
                    double distanceKm = driver.getLocation().distanceTo(ride.getSource());
                    if (distanceKm <= PROXIMITY_RADIUS_KM) {
                        System.out.printf("[RideDispatcher]: Notifying driver %s (%.2f km away) about Ride %s%n",
                                driver.getName(), distanceKm, ride.getId());
                    }
                }
            }
        });
    }

    public void shutdown() {
        dispatchPool.shutdown();
    }
}
