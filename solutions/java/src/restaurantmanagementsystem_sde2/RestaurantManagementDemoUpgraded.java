package restaurantmanagementsystem_sde2;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class RestaurantManagementDemoUpgraded {

    public static void main(String[] args) throws InterruptedException {
        Restaurant hellsKitchen = new Restaurant();

        // Build Physical Geometries
        hellsKitchen.addTable(new Table(1, 2)); // 2-Top (ID 1)
        hellsKitchen.addTable(new Table(2, 2)); // 2-Top (ID 2)
        hellsKitchen.addTable(new Table(3, 4)); // 4-Top (ID 3)
        hellsKitchen.addTable(new Table(4, 8)); // 8-Top (ID 4)

        System.out.println("--- Table Geometry Algorithmic SDE3 Check ---");
        // A party of 3 arrives. The Strategy should skip the 2-Tops, and correctly seize the 4-top!
        Reservation reservation1 = new Reservation(101, "Alice Gordon", 3, LocalDateTime.now());
        hellsKitchen.makeReservation(reservation1);
        
        // A party of 2 arrives. The strategy should seize a 2-top.
        Reservation reservation2 = new Reservation(102, "Bob Smith", 2, LocalDateTime.now());
        hellsKitchen.makeReservation(reservation2);

        // A party of 4 arrives. The 4-top is OCCUPIED by Alice! 
        // It must fallback and allocate the 8-top to prevent losing the sale!
        Reservation reservation3 = new Reservation(103, "Charlie Davis", 4, LocalDateTime.now());
        hellsKitchen.makeReservation(reservation3);

        System.out.println("\\n--- Asynchronous Observer Validation ---");
        MenuItem burger = new MenuItem(1, "Wagyu Burger", "Beef", new BigDecimal("25.99"));
        MenuItem fries = new MenuItem(2, "Truffle Fries", "Potato", new BigDecimal("12.99"));
        
        // The total invoice price evaluates cleanly via BigDecimals
        BigDecimal total = burger.getPrice().add(fries.getPrice());
        Order order = new Order(500, List.of(burger, fries), total, OrderStatus.PENDING);
        
        // Execution
        System.out.println("Waiter presses submit button at POS Terminal...");
        hellsKitchen.placeOrder(order);
        System.out.println("Waiter is immediately free to handle another table. They are completely unblocked!");

        // The Kitchen completes the meal 2 seconds later
        Thread.sleep(1000);
        hellsKitchen.updateOrderStatus(500, OrderStatus.READY);
        
        // Await the background Pager notification.
        Thread.sleep(1000);
        hellsKitchen.shutdownDispatcher();
    }
}
