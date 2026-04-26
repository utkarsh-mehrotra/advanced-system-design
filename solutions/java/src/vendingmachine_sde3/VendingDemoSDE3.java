package vendingmachine_sde3;

public class VendingDemoSDE3 {
    public static void main(String[] args) {
        System.out.println("--- Booting Vending Machine SDE3 (Lock-Free Event-Driven) ---");

        VendingController controller = new VendingController();
        controller.addProduct("A1", new Product("Soda", 1.50));

        // Testing the lock-free CAS insert -> select -> dispense cycle
        controller.insertCoin(Coin.QUARTER);
        controller.insertCoin(Coin.QUARTER);
        controller.insertCoin(Coin.QUARTER);
        controller.insertCoin(Coin.QUARTER);
        controller.insertCoin(Coin.QUARTER); // Total 1.25
        
        // This will be rejected silently or via state, requiring more money
        controller.selectProduct("A1");
        
        // Add last quarter
        controller.insertCoin(Coin.QUARTER); // Total 1.50
        
        // Should succeed and trigger async motor/change events
        controller.selectProduct("A1");
    }
}
