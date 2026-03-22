package vendingmachine_sde2;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class VendingMachine {
    
    // Core SDE3 Change: NOT a Singleton!
    private final String machineId;
    
    private final Inventory inventory;
    private final VendingMachineState idleState;
    private final VendingMachineState readyState;
    private final VendingMachineState dispenseState;
    private final VendingMachineState returnChangeState;
    
    private VendingMachineState currentState;
    private Product selectedProduct;
    private double totalPayment;
    
    // Core SDE3 Change: Strict lock guarding the entire machine interaction session
    // prevents 2 "connected" mobile app users from interleaving their insert/dispense commands.
    private final Lock sessionLock;

    public VendingMachine(String machineId) {
        this.machineId = machineId;
        this.inventory = new Inventory();
        
        this.idleState = new IdleState(this);
        this.readyState = new ReadyState(this);
        this.dispenseState = new DispenseState(this);
        this.returnChangeState = new ReturnChangeState(this);
        
        this.currentState = idleState;
        this.selectedProduct = null;
        this.totalPayment = 0.0;
        this.sessionLock = new ReentrantLock();
    }

    // Wrap state-mutating actions in the session lock
    public void selectProduct(Product product) {
        sessionLock.lock();
        try {
            currentState.selectProduct(product);
        } finally {
            sessionLock.unlock();
        }
    }

    public void insertCoin(Coin coin) {
        sessionLock.lock();
        try {
            currentState.insertCoin(coin);
        } finally {
            sessionLock.unlock();
        }
    }

    public void insertNote(Note note) {
        sessionLock.lock();
        try {
            currentState.insertNote(note);
        } finally {
            sessionLock.unlock();
        }
    }

    public void dispenseProduct() {
        sessionLock.lock();
        try {
            currentState.dispenseProduct();
        } finally {
            sessionLock.unlock();
        }
    }

    public void returnChange() {
        sessionLock.lock();
        try {
            currentState.returnChange();
        } finally {
            sessionLock.unlock();
        }
    }

    // Package-private mutators for the State classes
    void setState(VendingMachineState state) {
        this.currentState = state;
    }

    VendingMachineState getIdleState() { return idleState; }
    VendingMachineState getReadyState() { return readyState; }
    VendingMachineState getDispenseState() { return dispenseState; }
    VendingMachineState getReturnChangeState() { return returnChangeState; }

    Product getSelectedProduct() { return selectedProduct; }
    void setSelectedProduct(Product product) { this.selectedProduct = product; }
    void resetSelectedProduct() { this.selectedProduct = null; }

    double getTotalPayment() { return totalPayment; }
    void addCoin(Coin coin) { totalPayment += coin.getValue(); }
    void addNote(Note note) { totalPayment += note.getValue(); }
    void resetPayment() { totalPayment = 0.0; }

    public Inventory getInventory() { return inventory; }
    public String getMachineId() { return machineId; }
}
