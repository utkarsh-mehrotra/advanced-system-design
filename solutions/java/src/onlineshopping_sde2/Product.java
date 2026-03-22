package onlineshopping_sde2;

import java.util.concurrent.atomic.AtomicInteger;

public class Product {
    private final String id;
    private final String name;
    private final String description;
    private final double price;
    private final AtomicInteger quantity;

    public Product(String id, String name, String description, double price, int initialQuantity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = new AtomicInteger(initialQuantity);
    }

    public boolean tryDeductQuantity(int amount) {
        while (true) {
            int current = quantity.get();
            if (current < amount) {
                return false;
            }
            if (quantity.compareAndSet(current, current - amount)) {
                return true;
            }
        }
    }

    public void restoreQuantity(int amount) {
        quantity.addAndGet(amount);
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity.get(); }
}
