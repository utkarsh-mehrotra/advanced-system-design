package restaurantmanagementsystem_sde3;

public class KitchenDispatcher {
    public KitchenDispatcher() {
        EventBus.getInstance().subscribe("KITCHEN_ORDER", this::processKitchenTicket);
    }

    private void processKitchenTicket(Object payload) {
        System.out.println("KitchenDispatcher [Background Cook Routing]: Printing ticket for -> " + payload);
    }
}
