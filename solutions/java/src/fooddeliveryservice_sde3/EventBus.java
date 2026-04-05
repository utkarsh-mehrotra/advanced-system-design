package fooddeliveryservice_sde3;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class EventBus {
    private final Map<String, List<Consumer<Object>>> listeners = new ConcurrentHashMap<>();

    private static class Holder {
        private static final EventBus INSTANCE = new EventBus();
    }

    public static EventBus getInstance() {
        return Holder.INSTANCE;
    }

    public void subscribe(String topic, Consumer<Object> listener) {
        listeners.computeIfAbsent(topic, k -> new CopyOnWriteArrayList<>()).add(listener);
    }

    public void publish(String topic, Object payload) {
        List<Consumer<Object>> topicListeners = listeners.get(topic);
        if (topicListeners != null) {
            topicListeners.forEach(listener -> listener.accept(payload));
        }
    }
}
