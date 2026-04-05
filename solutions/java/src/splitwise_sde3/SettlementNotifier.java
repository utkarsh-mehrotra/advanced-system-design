package splitwise_sde3;

public class SettlementNotifier {
    public SettlementNotifier() {
        EventBus.getInstance().subscribe("BALANCE_UPDATED", this::pushNotification);
    }

    private void pushNotification(Object payload) {
        System.out.println("SettlementNotifier [Async Push]: Sending Mobile Warning -> " + payload);
    }
}
