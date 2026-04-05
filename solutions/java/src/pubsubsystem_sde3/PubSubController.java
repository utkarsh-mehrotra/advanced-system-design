package pubsubsystem_sde3;

public class PubSubController {
    public void registerSubscriber(String topic, Subscriber sub) {
        PubSubBroker.getInstance().subscribe(topic, sub::consume);
    }

    public void fireEvent(String topic, Object payload) {
        PubSubBroker.getInstance().publish(new Message(topic, payload));
    }
}
