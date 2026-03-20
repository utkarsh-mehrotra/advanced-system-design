package pubsubsystem_upgraded;

public class Publisher {
    private final MessageBroker broker;

    public Publisher(MessageBroker broker) {
        this.broker = broker;
    }

    // Fire-and-forget payload delivery to the Broker.
    // The publisher delegates entirely and never waits for consumer processing.
    public void publish(Topic topic, Message message) {
        System.out.println("[" + Thread.currentThread().getName() + "] Publisher submitting message: " + message.getContent() + " to topic: " + topic.getName());
        broker.publish(topic, message);
    }
}
