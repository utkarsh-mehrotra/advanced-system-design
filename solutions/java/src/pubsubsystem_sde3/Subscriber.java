package pubsubsystem_sde3;

public class Subscriber {
    private final String id;

    public Subscriber(String id) {
        this.id = id;
    }

    public void consume(Message message) {
        System.out.println("Subscriber [" + id + "] processed message asynchronously: " + message.getPayload());
    }
}
