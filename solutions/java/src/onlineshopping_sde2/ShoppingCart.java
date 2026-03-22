package onlineshopping_sde2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ShoppingCart {
    private final Map<String, OrderItem> items;

    public ShoppingCart() {
        this.items = new ConcurrentHashMap<>();
    }

    public void addItem(Product product, int quantity) {
        // Safe concurrent update using compute
        items.compute(product.getId(), (id, existingItem) -> {
            if (existingItem != null) {
                return new OrderItem(product, existingItem.getQuantity() + quantity);
            } else {
                return new OrderItem(product, quantity); // Creates new instance
            }
        });
    }

    public void removeItem(String productId) {
        items.remove(productId);
    }

    public void updateItemQuantity(String productId, int quantity) {
        items.computeIfPresent(productId, (id, item) -> new OrderItem(item.getProduct(), quantity));
    }

    public List<OrderItem> getItems() {
        return new ArrayList<>(items.values());
    }

    public void clear() {
        items.clear();
    }
}
