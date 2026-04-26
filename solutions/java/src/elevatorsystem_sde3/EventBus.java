package elevatorsystem_sde3;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EventBus {
    private static final EventBus INSTANCE = new EventBus();
    private final Map<String, List<Runnable>> listeners = new ConcurrentHashMap<>();

    private EventBus() {}

    public static EventBus getInstance() {
        return INSTANCE;
    }

    public void subscribe(String eventType, Runnable action) {
        listeners.computeIfAbsent(eventType, k -> new ArrayList<>()).add(action);
    }

    public void publish(String eventType, Object payload) {
        List<Runnable> actions = listeners.get(eventType);
        if (actions != null) {
            System.out.println("[EventBus] Async Dispatch -> " + eventType + " | Payload: " + payload);
            actions.forEach(Runnable::run);
        }
    }
}
