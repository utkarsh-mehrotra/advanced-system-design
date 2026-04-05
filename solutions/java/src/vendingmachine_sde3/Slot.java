package vendingmachine_sde3;

import java.util.concurrent.atomic.AtomicInteger;

public class Slot {
    private final String id;
    private final double itemPrice;
    private final AtomicInteger quantity;

    public Slot(String id, double price, int qty) {
        this.id = id;
        this.itemPrice = price;
        this.quantity = new AtomicInteger(qty);
    }

    public String getId() { return id; }
    public double getPrice() { return itemPrice; }

    public boolean pushInventoryOut() {
        while (true) {
            int current = quantity.get();
            if (current <= 0) return false;
            if (quantity.compareAndSet(current, current - 1)) {
                return true;
            }
        }
    }
}
