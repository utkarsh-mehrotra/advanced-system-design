package vendingmachine_upgraded;

public class VendingMachineDemoUpgraded {
    public static void main(String[] args) {
        // SDE3: Fleet capability. We can manage multiple non-singleton instances!
        VendingMachine vm1 = new VendingMachine("VM-LOBBY-1");
        VendingMachine vm2 = new VendingMachine("VM-CAFE-1");

        Product coke = new Product("Coke", 1.50);
        Product pepsi = new Product("Pepsi", 1.50);
        Product water = new Product("Water", 1.00);

        // Configure local inventory for VM 1
        vm1.getInventory().addProduct(coke, 1);
        vm1.getInventory().addProduct(pepsi, 5);
        vm1.getInventory().addProduct(water, 3);

        System.out.println("--- Vending Machine 1 (Thread-Safe Transaction) ---");
        // State actions are universally wrapped with the ReentrantLock, making them perfectly safe
        vm1.selectProduct(coke);
        vm1.insertNote(Note.ONE);
        vm1.insertCoin(Coin.QUARTER);
        vm1.insertCoin(Coin.QUARTER);
        // Payment complete automatically triggers dispense waiting.
        vm1.dispenseProduct();
        vm1.returnChange();

        System.out.println("\n--- Out Of Stock Test (Concurrent Safe) ---");
        // Coke was 1, now bought. 
        vm1.selectProduct(coke); 

        System.out.println("\n--- Vending Machine 2 Isolated Operation ---");
        vm2.getInventory().addProduct(coke, 2);
        vm2.selectProduct(coke);
        vm2.insertNote(Note.FIVE);
        vm2.dispenseProduct();
        vm2.returnChange();
    }
}
