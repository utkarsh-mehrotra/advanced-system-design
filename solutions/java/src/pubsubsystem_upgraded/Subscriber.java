package pubsubsystem_upgraded;

public interface Subscriber {
    // Unique ID for the consumer/subscriber
    String getId();
    
    // Callback to process identical payload
    void onMessage(Message message);
}
