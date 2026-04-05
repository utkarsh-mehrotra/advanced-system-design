package fooddeliveryservice_sde3;

public class DeliveryDispatcher {
    public DeliveryDispatcher() {
        EventBus.getInstance().subscribe("ORDER_STATUS_CHANGED", this::handleStateTriggers);
    }

    private void handleStateTriggers(Object payload) {
        String event = (String) payload;
        if (event.contains(OrderStatus.PREPARING.name())) {
            System.out.println("DeliveryDispatcher [Async]: Searching for nearby agents for " + event.split(":")[0]);
        }
    }
}
