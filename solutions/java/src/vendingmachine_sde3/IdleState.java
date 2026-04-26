package vendingmachine_sde3;

public class IdleState implements VendingMachineState {
    @Override
    public VendingMachineState insertCoin(VendingController context, Coin coin) {
        return new ReadyState();
    }

    @Override
    public VendingMachineState insertNote(VendingController context, Note note) {
        return new ReadyState();
    }

    @Override
    public VendingMachineState selectProduct(VendingController context, Product product) {
        System.out.println("Please insert money first.");
        return this;
    }

    @Override
    public VendingMachineState dispenseProduct(VendingController context) {
        System.out.println("Please select a product first.");
        return this;
    }

    @Override
    public VendingMachineState returnChange(VendingController context) {
        System.out.println("No change to return.");
        return this;
    }
}
