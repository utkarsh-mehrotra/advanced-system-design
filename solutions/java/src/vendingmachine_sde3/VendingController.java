package vendingmachine_sde3;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

public class VendingController {
    private final Map<String, Product> inventory = new ConcurrentHashMap<>();
    private final AtomicReference<VendingMachineState> activeState = new AtomicReference<>(new IdleState());
    
    // Thread-safe accumulators
    private volatile double currentBalance = 0.0; 

    public void addProduct(String slotId, Product product) {
        inventory.put(slotId, product);
    }

    public void insertCoin(Coin coin) {
        while (true) {
            VendingMachineState current = activeState.get();
            VendingMachineState next = current.insertCoin(this, coin);
            if (activeState.compareAndSet(current, next)) {
                this.currentBalance += coin.getValue();
                EventBus.getInstance().publish("COIN_INSERTED", coin.getValue());
                break;
            }
        }
    }

    public void selectProduct(String slotId) {
        Product p = inventory.get(slotId);
        if (p == null) return;

        while (true) {
            VendingMachineState current = activeState.get();
            VendingMachineState next = current.selectProduct(this, p);
            
            if (activeState.compareAndSet(current, next)) {
                if (next instanceof DispenseState) {
                    processDispenseAndChange(p);
                }
                break;
            }
        }
    }
    
    private void processDispenseAndChange(Product p) {
        // Asynchronous/Decoupled hardware triggers
        this.currentBalance -= p.getPrice();
        EventBus.getInstance().publish("TRIGGER_MOTOR_DISPENSE", p.getName());
        
        // Lock-free shift to return change state
        while (true) {
            VendingMachineState current = activeState.get();
            VendingMachineState next = current.dispenseProduct(this);
            if (activeState.compareAndSet(current, next)) {
                if (this.currentBalance > 0) {
                    EventBus.getInstance().publish("RETURN_CHANGE", this.currentBalance);
                    this.currentBalance = 0.0;
                }
                
                // Finally, return to idle
                while (true) {
                    VendingMachineState changeState = activeState.get();
                    VendingMachineState idle = changeState.returnChange(this);
                    if (activeState.compareAndSet(changeState, idle)) {
                        break;
                    }
                }
                break;
            }
        }
    }

    public void addFunds(double amount) {
        this.currentBalance += amount;
    }

    public boolean hasSufficientFunds(Product product) {
        return this.currentBalance >= product.getPrice();
    }
}

