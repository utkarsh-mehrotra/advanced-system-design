package carrentalsystem_sde2;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class RentalSystem {
    private final Map<String, Car> inventory = new ConcurrentHashMap<>();
    private final Map<String, Reservation> reservations = new ConcurrentHashMap<>();
    private final ReentrantLock lock = new ReentrantLock();
    private final PricingStrategy pricingStrategy;

    public RentalSystem(PricingStrategy pricingStrategy) {
        this.pricingStrategy = pricingStrategy;
    }

    public void addCar(Car car) {
        inventory.put(car.getLicensePlate(), car);
    }

    public Optional<Reservation> bookCar(Customer customer, String licensePlate, int days) {
        lock.lock();
        try {
            Car car = inventory.get(licensePlate);
            if (car != null && car.isAvailable()) {
                car.setAvailable(false);
                double cost = pricingStrategy.calculatePrice(car, days);
                Reservation reservation = new Reservation(customer, car, days, cost);
                reservations.put(reservation.getReservationId(), reservation);
                return Optional.of(reservation);
            }
            return Optional.empty();
        } finally {
            lock.unlock();
        }
    }

    public boolean returnCar(String reservationId) {
        Reservation res = reservations.get(reservationId);
        if (res != null) {
            lock.lock();
            try {
                res.getCar().setAvailable(true);
                reservations.remove(reservationId);
                return true;
            } finally {
                lock.unlock();
            }
        }
        return false;
    }
}
