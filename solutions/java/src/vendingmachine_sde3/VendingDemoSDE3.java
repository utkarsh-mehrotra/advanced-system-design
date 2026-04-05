package vendingmachine_sde3;

public class VendingDemoSDE3 {
    public static void main(String[] args) {
        new HardwareDispenser(); // Boot detached motor listener

        VendingController controller = new VendingController();
        controller.loadSlot(new Slot("A1", 1.50, 5));

        System.out.println("User drops a quarter and a 2 dollar bill...");
        controller.insertCoin(2.25);
        
        System.out.println("User pushes A1...");
        controller.selectProduct("A1");
    }
}
