package ridesharingservice_sde3;

import java.util.concurrent.atomic.AtomicReference;

public class Ride {
    private final String rideId;
    private final AtomicReference<RideStatus> status;

    public Ride(String rideId) {
        this.rideId = rideId;
        this.status = new AtomicReference<>(RideStatus.REQUESTED);
    }

    public String getRideId() { return rideId; }
    public RideStatus getStatus() { return status.get(); }

    public boolean transitionStatus(RideStatus expected, RideStatus next) {
        if (status.compareAndSet(expected, next)) {
            EventBus.getInstance().publish("RIDE_STATE_CHANGE", rideId + ":" + next.name());
            return true;
        }
        return false;
    }
}
