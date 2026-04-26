package coffeevendingmachine_sde3;

import java.util.concurrent.locks.ReentrantLock;

public class CoffeeMachine {
    private CoffeeMachineState state = CoffeeMachineState.READY;
    private final ReentrantLock lock = new ReentrantLock();
    private int waterSupplyML = 5000;

    public void brewCoffee(String recipe) {
        lock.lock();
        try {
            if (state == CoffeeMachineState.READY && waterSupplyML >= 250) {
                state = CoffeeMachineState.BREWING;
                System.out.println("SDE2 Machine: Dispensing " + recipe);
                waterSupplyML -= 250;
                
                // Simulate physical dispense
                System.out.println("Click... Brew complete.");
                state = CoffeeMachineState.READY;
            } else if (waterSupplyML < 250) {
                state = CoffeeMachineState.OUT_OF_SUPPLIES;
                System.out.println("SDE2 Machine: Need maintenance.");
            } else {
                System.out.println("SDE2 Machine: Already brewing!");
            }
        } finally {
            lock.unlock();
        }
    }
}
