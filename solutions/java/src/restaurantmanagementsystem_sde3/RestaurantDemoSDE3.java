package restaurantmanagementsystem_sde3;

public class RestaurantDemoSDE3 {
    public static void main(String[] args) {
        new KitchenDispatcher(); // Boot async cooks

        RestaurantManager manager = new RestaurantManager();
        manager.addTable(new Table("T5"));

        manager.reserveTable("CUST_A", "T5");
        manager.reserveTable("CUST_B", "T5"); // CAS collision defense test
        
        manager.placeOrder("T5", "2x Pizza Margerita, 1x Coke");
    }
}
