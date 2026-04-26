package trafficsignalsystem_sde3;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class IntersectionController {
    private final Map<String, TrafficLight> lights = new ConcurrentHashMap<>();
    private final ReentrantLock transitionLock = new ReentrantLock();

    public void addLight(TrafficLight light) {
        lights.put(light.getId(), light);
    }

    public void transitionSignal(String lightId, SignalColor newColor) {
        // Enforce atomicity. In a real system, you'd check cross-traffic states here to prevent simultaneous greens.
        transitionLock.lock();
        try {
            TrafficLight light = lights.get(lightId);
            if (light != null) {
                System.out.println("Intersection: Changing " + lightId + " from " + light.getCurrentColor() + " to " + newColor);
                light.setCurrentColor(newColor);
            }
        } finally {
            transitionLock.unlock();
        }
    }
}
