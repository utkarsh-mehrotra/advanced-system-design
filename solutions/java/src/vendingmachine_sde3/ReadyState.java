package vendingmachine_sde3;

public class ReadyState implements VendingMachineState {
    @Override
    public VendingMachineState insertCoin(VendingController context, Coin coin) {
        return this; // stay in ready
    }

    @Override
    public VendingMachineState insertNote(VendingController context, Note note) {
        return this;
    }

    @Override
    public VendingMachineState selectProduct(VendingController context, Product product) {
        if (context.hasSufficientFunds(product)) {
            return new DispenseState(product);
        } else {
            System.out.println("Insufficient funds for product: " + product.getName());
            return this;
        }
    }

    @Override
    public VendingMachineState dispenseProduct(VendingController context) {
        System.out.println("Select a product first.");
        return this;
    }

    @Override
    public VendingMachineState returnChange(VendingController context) {
        return new ReturnChangeState();
    }
}
