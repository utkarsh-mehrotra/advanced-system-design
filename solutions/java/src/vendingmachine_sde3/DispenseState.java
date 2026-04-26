package vendingmachine_sde3;

public class DispenseState implements VendingMachineState {
    private final Product productToDispense;

    public DispenseState(Product productToDispense) {
        this.productToDispense = productToDispense;
    }

    public Product getProductToDispense() {
        return productToDispense;
    }

    @Override
    public VendingMachineState insertCoin(VendingController context, Coin coin) {
        System.out.println("Please wait, dispensing product.");
        return this;
    }

    @Override
    public VendingMachineState insertNote(VendingController context, Note note) {
        System.out.println("Please wait, dispensing product.");
        return this;
    }

    @Override
    public VendingMachineState selectProduct(VendingController context, Product product) {
        System.out.println("Product already selected.");
        return this;
    }

    @Override
    public VendingMachineState dispenseProduct(VendingController context) {
        return new ReturnChangeState();
    }

    @Override
    public VendingMachineState returnChange(VendingController context) {
        System.out.println("Dispensing product first.");
        return this;
    }
}
