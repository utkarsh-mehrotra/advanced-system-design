package trafficsignalsystem_sde3;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class IntersectionController {
    private final Map<String, TrafficLight> lights = new ConcurrentHashMap<>();

    public IntersectionController() {
        EventBus.getInstance().subscribe("EMERGENCY_VEHICLE_DETECTED", this::handleEmergency);
    }

    public void addLight(TrafficLight light) {
        lights.put(light.getId(), light);
    }

    private void handleEmergency(Object payload) {
        String axis = (String) payload; // e.g. "NORTH_SOUTH"
        System.out.println("IntersectionController: EMERGENCY DETECTED on " + axis + ". Overriding signals instantly.");
        
        lights.forEach((id, light) -> {
            if (id.equals(axis)) {
                light.forceEmergencyState(SignalState.EMERGENCY_OVERRIDE_GREEN);
            } else {
                light.forceEmergencyState(SignalState.EMERGENCY_OVERRIDE_RED);
            }
        });
    }
}
