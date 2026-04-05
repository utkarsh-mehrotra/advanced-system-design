package onlineshopping_sde3;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ShoppingCartManager {
    private final Map<String, InventoryItem> catalog = new ConcurrentHashMap<>();

    public void addCatalogItem(InventoryItem item) {
        catalog.put(item.getItemId(), item);
    }

    public void checkout(String userId, String itemId, int quantity) {
        InventoryItem item = catalog.get(itemId);
        if (item != null) {
            if (item.decrementStock(quantity)) {
                System.out.println("ShoppingCartManager: Lock-free checkout successful for " + userId);
                
                // Detach slow payment and invoicing networks
                EventBus.getInstance().publish("ORDER_CONFIRMED", userId + ":" + itemId + ":" + quantity);
            } else {
                System.out.println("ShoppingCartManager: Insufficient stock for " + userId);
            }
        }
    }
}
