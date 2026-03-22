package vendingmachine_sde2;

public class DispenseState implements VendingMachineState {
    private final VendingMachine machine;

    public DispenseState(VendingMachine machine) {
        this.machine = machine;
    }

    @Override
    public void selectProduct(Product product) {
        System.out.println("Product dispensing in progress...");
    }

    @Override
    public void insertCoin(Coin coin) {
        System.out.println("Product dispensing in progress. Cannot accept coins.");
    }

    @Override
    public void insertNote(Note note) {
        System.out.println("Product dispensing in progress. Cannot accept notes.");
    }

    @Override
    public void dispenseProduct() {
        Product product = machine.getSelectedProduct();
        
        // SDE3: Safe extraction. If another malicious concurrent checkout somehow passed 
        // to here despite outer locks, the lock-free inventory map acts as the final defense.
        boolean dispensed = machine.getInventory().decrementQuantitySafely(product);
        
        if (dispensed) {
            System.out.println("Dispensing product: " + product.getName());
            machine.setState(machine.getReturnChangeState());
        } else {
            // Highly unlikely due to structural context locking, but serves as fallback
            System.out.println("CRITICAL ERROR: Product ran out of stock during checkout.");
            machine.setState(machine.getReturnChangeState()); 
            // In returnChange, they'll get full refund since they didn't get the item
        }
    }

    @Override
    public void returnChange() {
        System.out.println("Please wait. Dispensing product first.");
    }
}
