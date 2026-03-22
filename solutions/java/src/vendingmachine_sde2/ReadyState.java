package vendingmachine_sde2;

public class ReadyState implements VendingMachineState {
    private final VendingMachine machine;

    public ReadyState(VendingMachine machine) {
        this.machine = machine;
    }

    @Override
    public void selectProduct(Product product) {
        System.out.println("Product already selected. Please make payment.");
    }

    @Override
    public void insertCoin(Coin coin) {
        machine.addCoin(coin);
        System.out.println("Coin inserted: " + coin);
        checkPaymentStatus();
    }

    @Override
    public void insertNote(Note note) {
        machine.addNote(note);
        System.out.println("Note inserted: " + note);
        checkPaymentStatus();
    }

    @Override
    public void dispenseProduct() {
        System.out.println("Please make full payment first.");
    }

    @Override
    public void returnChange() {
        double amountToReturn = machine.getTotalPayment();
        if (amountToReturn > 0) {
            System.out.println("Refunding payment: $" + amountToReturn);
            machine.resetPayment();
        }
        machine.resetSelectedProduct();
        machine.setState(machine.getIdleState());
    }

    private void checkPaymentStatus() {
        if (machine.getTotalPayment() >= machine.getSelectedProduct().getPrice()) {
            machine.setState(machine.getDispenseState());
        }
    }
}
