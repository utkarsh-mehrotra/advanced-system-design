package vendingmachine_sde3;

public class ReturnChangeState implements VendingMachineState {
    @Override
    public VendingMachineState insertCoin(VendingController context, Coin coin) {
        System.out.println("Please wait, returning change.");
        return this;
    }

    @Override
    public VendingMachineState insertNote(VendingController context, Note note) {
        System.out.println("Please wait, returning change.");
        return this;
    }

    @Override
    public VendingMachineState selectProduct(VendingController context, Product product) {
        System.out.println("Please wait, returning change.");
        return this;
    }

    @Override
    public VendingMachineState dispenseProduct(VendingController context) {
        System.out.println("Product already dispensed.");
        return this;
    }

    @Override
    public VendingMachineState returnChange(VendingController context) {
        return new IdleState();
    }
}
