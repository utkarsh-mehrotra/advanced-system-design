package fooddeliveryservice_sde3;

import java.util.concurrent.atomic.AtomicBoolean;

public class DeliveryAgent {
    private final String id;
    private final String name;
    private final String phone;
    
    // SDE3: Crucial upgrade. Guarantees that if 50 requests query this exact driver simultaneously, 
    // strictly only ONE will successfully receive true via Compare-And-Swap (CAS) CPU instruction.
    private final AtomicBoolean available;

    public DeliveryAgent(String id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.available = new AtomicBoolean(true); // Default immediately ready
    }

    /**
     * SDE3: Execution Context Guarantee.
     * Attempts to mutually exclusively lock the driver for an assignment.
     * @return true if successful, false if the driver was snatched by a parallel thread previously.
     */
    public boolean lockAssignment() {
        return available.compareAndSet(true, false);
    }
    
    public void releaseAssignment() {
        available.set(true);
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public boolean isAvailable() { return available.get(); }
}
