package ridesharingservice_sde3;

import java.math.BigDecimal;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * SDE3: AtomicBoolean Driver + explicit per-Ride Lock preventing multi-driver acceptance race.
 */
public class Ride {
    private final String id;
    private final Passenger passenger;
    private final Location source;
    private final Location destination;

    private volatile Driver driver;
    private volatile RideStatus status;
    private volatile BigDecimal fare;

    // SDE3: Per-Ride mutation lock prevents "Lost Driver" double-accept bug
    private final Lock rideLock = new ReentrantLock();

    public Ride(String id, Passenger passenger, Location source, Location destination) {
        this.id = id;
        this.passenger = passenger;
        this.source = source;
        this.destination = destination;
        this.status = RideStatus.REQUESTED;
    }

    /**
     * SDE3 Atomic driver assignment.
     * @return true ONLY if this thread is the first to assign a driver; all others bounce.
     */
    public boolean assignDriverAtomically(Driver candidate) {
        rideLock.lock();
        try {
            if (this.status != RideStatus.REQUESTED) {
                return false; // Already claimed by another thread
            }
            if (!candidate.claimForRide()) {
                return false; // Driver was snatched by another Ride simultaneously
            }
            this.driver = candidate;
            this.status = RideStatus.DRIVER_ASSIGNED;
            return true;
        } finally {
            rideLock.unlock();
        }
    }

    public synchronized boolean startRide() {
        if (status == RideStatus.DRIVER_ASSIGNED) {
            status = RideStatus.IN_PROGRESS;
            return true;
        }
        return false;
    }

    public synchronized boolean completeRide(BigDecimal calculatedFare) {
        if (status == RideStatus.IN_PROGRESS) {
            this.fare = calculatedFare;
            this.status = RideStatus.COMPLETED;
            if (driver != null) driver.releaseFromRide();
            return true;
        }
        return false;
    }

    public synchronized boolean cancelRide() {
        if (status == RideStatus.REQUESTED || status == RideStatus.DRIVER_ASSIGNED) {
            status = RideStatus.CANCELLED;
            if (driver != null) driver.releaseFromRide();
            return true;
        }
        return false;
    }

    public String getId() { return id; }
    public Passenger getPassenger() { return passenger; }
    public Driver getDriver() { return driver; }
    public Location getSource() { return source; }
    public Location getDestination() { return destination; }
    public RideStatus getStatus() { return status; }
    public BigDecimal getFare() { return fare; }
}
