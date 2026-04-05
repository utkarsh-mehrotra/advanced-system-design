package coffeevendingmachine_sde3;

public class HardwareDispenser {
    public HardwareDispenser() {
        EventBus.getInstance().subscribe("DISPENSE_BEVERAGE", this::runMotor);
    }

    private void runMotor(Object payload) {
        System.out.println("HardwareDispenser [Async Mechanical]: Steaming and dispensing -> " + payload);
    }
}
