package vendingmachine_sde3;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

public class VendingController {
    private final Map<String, Slot> grid = new ConcurrentHashMap<>();
    private final AtomicReference<MachineState> activeState = new AtomicReference<>(MachineState.IDLE);
    private volatile double currentBalance = 0.0; // Volatile for thread safe primitive reading

    public void loadSlot(Slot s) {
        grid.put(s.getId(), s);
    }

    public void insertCoin(double coin) {
        if (activeState.compareAndSet(MachineState.IDLE, MachineState.COIN_INSERTED) || 
            activeState.get() == MachineState.COIN_INSERTED) {
            
            this.currentBalance += coin;
            System.out.println("VendingTracker: Inserted $" + coin + ". Balance is $" + currentBalance);
        }
    }

    public void selectProduct(String slotId) {
        if (activeState.get() != MachineState.COIN_INSERTED) return;

        Slot s = grid.get(slotId);
        if (s != null && currentBalance >= s.getPrice()) {
            if (activeState.compareAndSet(MachineState.COIN_INSERTED, MachineState.DISPENSING)) {
                if (s.pushInventoryOut()) {
                    currentBalance -= s.getPrice();
                    EventBus.getInstance().publish("TRIGGER_MOTOR", slotId);
                    
                    // Reset
                    activeState.set(MachineState.IDLE);
                } else {
                    System.out.println("VendingTracker: Out of stock! Refunding..." + currentBalance);
                    activeState.set(MachineState.IDLE);
                }
            }
        }
    }
}
