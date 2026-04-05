package pubsubsystem_sde3;

public class Logger {
    public Logger() {
        PubSubBroker.getInstance().subscribe("SYSTEM_ALERTS", this::logAlert);
    }

    private void logAlert(Message message) {
        System.out.println("Logger [Background]: Received alert payload -> " + message.getPayload());
    }
}
