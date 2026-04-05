package ridesharingservice_sde3;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RideDispatcher {
    private final Map<String, Ride> rides = new ConcurrentHashMap<>();

    public void requestRide(Ride ride) {
        rides.put(ride.getRideId(), ride);
        EventBus.getInstance().publish("RIDE_REQUESTED", ride.getRideId());
    }

    public void updateRideState(String rideId, RideStatus expected, RideStatus next) {
        Ride r = rides.get(rideId);
        if (r != null) {
            if (!r.transitionStatus(expected, next)) {
                System.out.println("RideDispatcher: State transition denied for " + rideId + " (Likely stale cache)");
            }
        }
    }
}
