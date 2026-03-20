package vendingmachine_upgraded;

public class ReturnChangeState implements VendingMachineState {
    private final VendingMachine machine;

    public ReturnChangeState(VendingMachine machine) {
        this.machine = machine;
    }

    @Override
    public void selectProduct(Product product) {
        System.out.println("Please collect your change first.");
    }

    @Override
    public void insertCoin(Coin coin) {
        System.out.println("Please collect your change first.");
    }

    @Override
    public void insertNote(Note note) {
        System.out.println("Please collect your change first.");
    }

    @Override
    public void dispenseProduct() {
        System.out.println("Product already dispensed.");
    }

    @Override
    public void returnChange() {
        double change = machine.getTotalPayment() - machine.getSelectedProduct().getPrice();
        if (change > 0) {
            System.out.printf("Returning change: $%.2f%n", change);
        } else {
            System.out.println("No change to return.");
        }
        
        machine.resetPayment();
        machine.resetSelectedProduct();
        machine.setState(machine.getIdleState());
    }
}
