package pubsubsystem_sde3;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class PubSubBroker {
    private final Map<String, List<Consumer<Message>>> topics = new ConcurrentHashMap<>();

    private static class Holder {
        private static final PubSubBroker INSTANCE = new PubSubBroker();
    }

    public static PubSubBroker getInstance() {
        return Holder.INSTANCE;
    }

    public void subscribe(String topic, Consumer<Message> listener) {
        topics.computeIfAbsent(topic, k -> new CopyOnWriteArrayList<>()).add(listener);
    }

    public void publish(Message message) {
        List<Consumer<Message>> topicListeners = topics.get(message.getTopic());
        if (topicListeners != null) {
            // Concurrent execution iterator using CopyOnWriteArrayList
            topicListeners.forEach(listener -> listener.accept(message));
        }
    }
}
