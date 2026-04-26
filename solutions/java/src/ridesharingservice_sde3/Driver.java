package ridesharingservice_sde3;

import java.util.concurrent.atomic.AtomicBoolean;

public class Driver {
    private final String id;
    private final String name;
    private final String licensePlate;
    private volatile Location location;

    // SDE3: Replaces a plain enum, preventing concurrent TOCTOU booking race
    private final AtomicBoolean available;

    public Driver(String id, String name, String licensePlate, Location location) {
        this.id = id;
        this.name = name;
        this.licensePlate = licensePlate;
        this.location = location;
        this.available = new AtomicBoolean(true);
    }

    /**
     * Thread-safe CAS dispatch claim.
     * @return true if this driver was successfully claimed; false if another thread beat us to it.
     */
    public boolean claimForRide() {
        return available.compareAndSet(true, false);
    }

    public void releaseFromRide() {
        available.set(true);
    }

    public boolean isAvailable() { return available.get(); }
    public String getId() { return id; }
    public String getName() { return name; }
    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }
}
