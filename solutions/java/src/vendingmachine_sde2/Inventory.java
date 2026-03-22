package vendingmachine_sde2;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Inventory {
    private final Map<Product, Integer> products;

    public Inventory() {
        products = new ConcurrentHashMap<>();
    }

    public void addProduct(Product product, int quantity) {
        // Safe addition
        products.compute(product, (k, currentVal) -> 
            (currentVal == null) ? quantity : currentVal + quantity
        );
    }

    public void removeProduct(Product product) {
        products.remove(product);
    }

    /**
     * SDE3: Crucial Fix.
     * Decrements inventory using a single lock-free atomic transaction via computeIfPresent.
     * In the original, thread 1 could see val > 0, then thread 2 sees val > 0, 
     * both decrement, resulting in negative phantom inventory!
     */
    public boolean decrementQuantitySafely(Product product) {
        // We capture the old value before mutating. Since computeIfPresent is atomic per key,
        // it acts as a perfect Compare-And-Swap mechanism for inventory dispensing.
        final boolean[] success = new boolean[1];
        
        products.computeIfPresent(product, (p, currentTokens) -> {
            if (currentTokens > 0) {
                success[0] = true;
                return currentTokens - 1;
            } else {
                return currentTokens; // Retain 0
            }
        });

        return success[0];
    }

    public int getQuantity(Product product) {
        return products.getOrDefault(product, 0);
    }

    public boolean isAvailable(Product product) {
        return products.getOrDefault(product, 0) > 0;
    }
}
