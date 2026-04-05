package restaurantmanagementsystem_sde3;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RestaurantManager {
    private final Map<String, Table> tables = new ConcurrentHashMap<>();

    public void addTable(Table table) {
        tables.put(table.getTableId(), table);
    }

    public void reserveTable(String customerId, String tableId) {
        Table table = tables.get(tableId);
        if (table != null && table.reserve()) {
            System.out.println("RestaurantManager: Table " + tableId + " reserved dynamically for " + customerId);
        } else {
            System.out.println("RestaurantManager: Concurrent Reservation Blocked on Table " + tableId);
        }
    }

    public void placeOrder(String tableId, String orderDetails) {
        EventBus.getInstance().publish("KITCHEN_ORDER", tableId + ":" + orderDetails);
    }
}
