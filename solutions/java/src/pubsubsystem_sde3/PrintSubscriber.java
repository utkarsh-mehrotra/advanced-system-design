package pubsubsystem_sde3;

public class PrintSubscriber implements Subscriber {
    private final String name;
    private final int simulatedDelayMs;

    public PrintSubscriber(String name, int simulatedDelayMs) {
        this.name = name;
        this.simulatedDelayMs = simulatedDelayMs;
    }

    @Override
    public String getId() {
        return name;
    }

    @Override
    public void onMessage(Message message) {
        try {
            // SDE3: Simulating a slow consumer (e.g. database write or API call)
            // In the original system, this Thread.sleep() would have completely locked the Publisher!
            Thread.sleep(simulatedDelayMs);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("[" + Thread.currentThread().getName() + "] Subscriber " + name + " processed message: " + message.getContent());
    }
}
