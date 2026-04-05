package carrentalsystem_sde3;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class Reservation {
    private final String id;
    private final String customerId;
    private final String carLicensePlate;
    private final AtomicReference<State> state;

    public Reservation(String customerId, String carLicensePlate) {
        this.id = UUID.randomUUID().toString();
        this.customerId = customerId;
        this.carLicensePlate = carLicensePlate;
        this.state = new AtomicReference<>(State.PENDING);
    }

    public boolean transitionState(State expected, State next) {
        return state.compareAndSet(expected, next);
    }

    public State getState() { return state.get(); }
    public String getId() { return id; }
    public String getCarLicensePlate() { return carLicensePlate; }
}
