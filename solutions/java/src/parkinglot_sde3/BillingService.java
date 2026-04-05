package parkinglot_sde3;

public class BillingService {
    public BillingService() {
        EventBus.getInstance().subscribe("SPOT_VACATED", this::calculateBill);
    }

    private void calculateBill(Object payload) {
        System.out.println("BillingService [Async Workflow]: Calculating Exit Charge for empty spot -> " + payload);
    }
}
