package carrentalsystem_sde3;

public class Car {
    private final String licensePlate;
    private final String model;
    private volatile boolean isAvailable; // Volatile for fast reads without locks

    public Car(String licensePlate, String model) {
        this.licensePlate = licensePlate;
        this.model = model;
        this.isAvailable = true;
    }

    public String getLicensePlate() { return licensePlate; }
    public String getModel() { return model; }
    public boolean isAvailable() { return isAvailable; }
    
    // Package private - should only be modified by InventoryManager
    void setAvailable(boolean available) { 
        this.isAvailable = available; 
    }
}
