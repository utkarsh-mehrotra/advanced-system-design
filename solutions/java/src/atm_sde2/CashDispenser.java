package atm_sde2;

import java.util.concurrent.atomic.AtomicInteger;

public class CashDispenser {
    // SDE3: Hardware atomic integer for fast non-blocking concurrency
    private final AtomicInteger cashAvailable;

    public CashDispenser(int initialCash) {
        this.cashAvailable = new AtomicInteger(initialCash);
    }

    public boolean canDispense(int amount) {
        return cashAvailable.get() >= amount;
    }

    /**
     * Dispenses cash physically. Simulates a mechanically jam-susceptible operation.
     */
    public void dispenseCash(int amount) {
        // We use an optimistic spin-loop to acquire the cash deduction atomically
        while (true) {
            int current = cashAvailable.get();
            if (current < amount) {
                throw new IllegalStateException("Hardware Halt: ATM Out of physical cash notes.");
            }
            if (cashAvailable.compareAndSet(current, current - amount)) {
                // For demonstration, simulating a 10% chance of mechanical jam
                if (Math.random() < 0.10) {
                    // Critical failure during physical dispersion!
                    throw new RuntimeException("Hardware Jam inside Dispenser. Money was NOT released.");
                }
                
                System.out.println("[$ " + amount + " physically dispensed to tray...]");
                break;
            }
        }
    }
    
    public int getAvailableCash() {
        return cashAvailable.get();
    }
}
