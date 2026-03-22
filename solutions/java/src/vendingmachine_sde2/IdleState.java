package vendingmachine_sde2;

public class IdleState implements VendingMachineState {
    private final VendingMachine machine;

    public IdleState(VendingMachine machine) {
        this.machine = machine;
    }

    @Override
    public void selectProduct(Product product) {
        if (machine.getInventory().isAvailable(product)) {
            machine.setSelectedProduct(product);
            machine.setState(machine.getReadyState());
            System.out.println("Product selected: " + product.getName() + " for $" + product.getPrice());
        } else {
            System.out.println("Product out of stock: " + product.getName());
        }
    }

    @Override
    public void insertCoin(Coin coin) {
        System.out.println("Please select a product first.");
    }

    @Override
    public void insertNote(Note note) {
        System.out.println("Please select a product first.");
    }

    @Override
    public void dispenseProduct() {
        System.out.println("Please select a product and make a payment.");
    }

    @Override
    public void returnChange() {
        System.out.println("No payment made.");
    }
}
