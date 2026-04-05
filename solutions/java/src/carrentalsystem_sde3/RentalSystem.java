package carrentalsystem_sde3;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class RentalSystem {
    private final Map<String, Car> inventory = new ConcurrentHashMap<>();
    private final Map<String, Reservation> reservations = new ConcurrentHashMap<>();
    
    // ReadWriteLock allows multiple concurrent reads for inventory browsing
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();

    public RentalSystem() {
        EventBus.getInstance().subscribe("PAYMENT_SUCCESS", this::handlePaymentSuccess);
    }

    public void addCar(Car car) {
        inventory.put(car.getLicensePlate(), car);
    }

    public Optional<Reservation> initiateBooking(String customerId, String licensePlate) {
        rwLock.writeLock().lock();
        try {
            Car car = inventory.get(licensePlate);
            if (car != null && car.isAvailable()) {
                car.setAvailable(false);
                Reservation res = new Reservation(customerId, licensePlate);
                reservations.put(res.getId(), res);
                
                // Publish event to decouple payment processing
                EventBus.getInstance().publish("BOOKING_INITIATED", res);
                return Optional.of(res);
            }
            return Optional.empty();
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    private void handlePaymentSuccess(Object eventPayload) {
        if (eventPayload instanceof String) {
            String resId = (String) eventPayload;
            Reservation res = reservations.get(resId);
            if (res != null && res.transitionState(State.PENDING, State.CONFIRMED)) {
                System.out.println("RentalSystem: Reservation " + resId + " confirmed asynchronously via EventBus.");
            }
        }
    }
}
