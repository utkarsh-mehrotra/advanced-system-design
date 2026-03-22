package socialnetworkingservice_sde3.messaging;

import socialnetworkingservice_sde3.model.events.PostCreatedEvent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * MessageBroker (Simulates Kafka topic `posts.outbound`)
 */
public class MessageBus {
    private static final MessageBus INSTANCE = new MessageBus();
    
    // Simulates a Kafka topic with multiple consumers potentially reading from it
    private final BlockingQueue<PostCreatedEvent> postEvents = new LinkedBlockingQueue<>();

    private MessageBus() {}

    public static MessageBus getInstance() {
        return INSTANCE;
    }

    public void publishPostCreated(PostCreatedEvent event) {
        postEvents.offer(event);
    }

    public BlockingQueue<PostCreatedEvent> subscribeToPostCreatedEvents() {
        return postEvents; // In reality, Kafka allows consumer groups to independently offset
    }
}
