package vendingmachine_sde3;

public class HardwareDispenser {
    public HardwareDispenser() {
        EventBus.getInstance().subscribe("TRIGGER_MOTOR", this::dispenseItem);
    }

    private void dispenseItem(Object payload) {
        System.out.println("HardwareDispenser [Async Mechanical]: Activating Coil Motor for Slot -> " + payload);
    }
}
