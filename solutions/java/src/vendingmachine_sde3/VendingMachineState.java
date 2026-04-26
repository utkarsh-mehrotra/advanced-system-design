package vendingmachine_sde3;

public interface VendingMachineState {
    VendingMachineState insertCoin(VendingController context, Coin coin);
    VendingMachineState insertNote(VendingController context, Note note);
    VendingMachineState selectProduct(VendingController context, Product product);
    VendingMachineState dispenseProduct(VendingController context);
    VendingMachineState returnChange(VendingController context);
}
