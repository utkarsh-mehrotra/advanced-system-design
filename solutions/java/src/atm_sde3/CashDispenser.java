package atm_sde3;

public class CashDispenser {
    public CashDispenser() {
        EventBus.getInstance().subscribe("DISPENSE_CASH", this::dispense);
    }

    private void dispense(Object payload) {
        Double amount = (Double) payload;
        System.out.println("CashDispenser [Hardware Async]: Dispensing $" + amount + " to the trap box.");
    }
}
