package pubsubsystem_sde3;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Core SDE3 Component: The Async MessageBroker.
 * Decouples publishers from subscribers by absorbing messages instantly
 * and delivering them to subscribers on dedicated worker threads.
 */
public class MessageBroker {
    private final Map<String, Topic> topics;
    
    // Thread pool to handle asynchronous message deliveries.
    // In a massive system, you'd configure thread counts based on expected load.
    private final ExecutorService executorService;

    public MessageBroker() {
        this.topics = new ConcurrentHashMap<>();
        // Arbitrary thread pool size for this demonstration
        this.executorService = Executors.newFixedThreadPool(5);
    }

    public void createTopic(String topicName) {
        topics.putIfAbsent(topicName, new Topic(topicName));
    }

    public Topic getTopic(String topicName) {
        return topics.get(topicName);
    }

    public void subscribe(String topicName, Subscriber subscriber) {
        Topic topic = getTopic(topicName);
        if (topic != null) {
            topic.addSubscriber(subscriber);
            System.out.println("Subscriber " + subscriber.getId() + " joined topic: " + topicName);
        } else {
            System.out.println("Topic " + topicName + " does not exist.");
        }
    }

    /**
     * O(1) Blocking operation for the Publisher. 
     * The payload is handed off to worker background threads instantly.
     */
    public void publish(Topic topic, Message message) {
        if (!topics.containsKey(topic.getName())) {
            System.out.println("Unknown topic: " + topic.getName());
            return;
        }

        // Instead of a direct synchronous loop (topic.publish()), 
        // we submit independent delivery tasks for each subscriber to the Executor pool!
        for (Subscriber subscriber : topic.getSubscribers()) {
            executorService.submit(() -> {
                subscriber.onMessage(message);
            });
        }
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
