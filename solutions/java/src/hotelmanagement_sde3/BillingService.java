package hotelmanagement_sde3;

public class BillingService {
    public BillingService() {
        EventBus.getInstance().subscribe("GENERATE_BILL", this::processBill);
    }

    private void processBill(Object payload) {
        System.out.println("BillingService [Async Workflow]: Emitting PDF via Payment Gateway payload -> " + payload);
    }
}
