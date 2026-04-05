package onlineshopping_sde3;

import java.util.concurrent.atomic.AtomicInteger;

public class InventoryItem {
    private final String itemId;
    private final double price;
    private final AtomicInteger stockQuantity;

    public InventoryItem(String itemId, double price, int initialStock) {
        this.itemId = itemId;
        this.price = price;
        this.stockQuantity = new AtomicInteger(initialStock);
    }

    public String getItemId() { return itemId; }
    public double getPrice() { return price; }
    public int getStock() { return stockQuantity.get(); }

    public boolean decrementStock(int count) {
        while (true) {
            int current = stockQuantity.get();
            if (current < count) {
                return false; // Out of stock during flash sale
            }
            if (stockQuantity.compareAndSet(current, current - count)) {
                return true;
            }
        }
    }
    
    public void incrementStock(int count) {
        stockQuantity.addAndGet(count);
    }
}
