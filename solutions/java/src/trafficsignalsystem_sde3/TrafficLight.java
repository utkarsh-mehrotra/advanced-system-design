package trafficsignalsystem_sde3;

import java.util.concurrent.atomic.AtomicReference;

public class TrafficLight {
    private final String id;
    private final AtomicReference<SignalState> state;

    public TrafficLight(String id) {
        this.id = id;
        this.state = new AtomicReference<>(SignalState.RED);
    }

    public String getId() { return id; }
    public SignalState getState() { return state.get(); }

    public boolean transition(SignalState expected, SignalState next) {
        return state.compareAndSet(expected, next);
    }

    public void forceEmergencyState(SignalState emergencyState) {
        state.set(emergencyState); // Hard override bypasses CAS protection
    }
}
