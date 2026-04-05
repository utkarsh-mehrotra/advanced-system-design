package coffeevendingmachine_sde3;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class CoffeeController {
    private final AtomicReference<MachineState> state = new AtomicReference<>(MachineState.READY);
    private final AtomicInteger waterSupplyML = new AtomicInteger(5000);

    public void requestBrew(String recipe) {
        if (state.compareAndSet(MachineState.READY, MachineState.DISPENSING)) {
            while (true) {
                int currentWater = waterSupplyML.get();
                if (currentWater < 250) {
                    state.set(MachineState.OUT_OF_SUPPLIES);
                    return;
                }
                if (waterSupplyML.compareAndSet(currentWater, currentWater - 250)) {
                    // Fire-and-forget publish to actual physical hardware
                    EventBus.getInstance().publish("DISPENSE_BEVERAGE", recipe);
                    
                    // Allow UI to be free while physical dispensing starts asynchronously
                    state.set(MachineState.READY);
                    return;
                }
            }
        }
    }
}
