package restaurantmanagementsystem_upgraded;

import restaurantmanagementsystem_upgraded.payment.Payment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Replaces the old Singleton.
 * De-coupled Service handling async notifications and structural table dispatching.
 */
public class Restaurant {
    private final List<MenuItem> menu;
    private final Map<Integer, Order> orders;
    
    // NEW Domain Objects
    private final List<Table> tables;
    private final Map<Integer, Reservation> reservations;
    private final Map<Integer, Table> reservationToTableMap; // Bi-directional topological graph mapping

    private final Map<String, Payment> payments;
    private final EventDispatcher dispatcher;
    private final TableAllocationStrategy allocationStrategy;

    public Restaurant() {
        menu = new CopyOnWriteArrayList<>();
        orders = new ConcurrentHashMap<>();
        tables = new CopyOnWriteArrayList<>();
        reservations = new ConcurrentHashMap<>();
        reservationToTableMap = new ConcurrentHashMap<>();
        payments = new ConcurrentHashMap<>();
        
        // SDE3 Components
        dispatcher = new EventDispatcher();
        allocationStrategy = new TableAllocationStrategy();
    }

    /* --- Menu & Setup --- */
    public void addMenuItem(MenuItem item) { menu.add(item); }
    public void addTable(Table table) { tables.add(table); }

    /* --- The Table Allocation Router --- */
    public synchronized boolean makeReservation(Reservation reservation) {
        reservations.put(reservation.getId(), reservation);
        
        // Strategy executes the bounding topology algorithm
        Table optimalTable = allocationStrategy.allocateOptimalTable(tables, reservation.getPartySize());
        
        if (optimalTable != null) {
            // SDE3: Atomic Binding! 
            // Ensures even if the lock-free stream saw it as available, we actually reserve it here!
            if (optimalTable.lockAndOccupy()) {
                reservationToTableMap.put(reservation.getId(), optimalTable);
                System.out.println("Success: Reservation " + reservation.getId() + " topologically mapped to Table ID " + optimalTable.getTableId() + " (Capacity " + optimalTable.getCapacity() + ")");
                return true;
            }
        }
        
        System.out.println("Waitlist: No available tables can optimally fit party of size " + reservation.getPartySize());
        return false;
    }

    public void releaseReservation(int reservationId) {
        Table linkedTable = reservationToTableMap.remove(reservationId);
        if (linkedTable != null) {
            linkedTable.releaseTable(); // Safe unlock
            System.out.println("Table ID " + linkedTable.getTableId() + " has been cleaned and released for new allocations.");
        }
    }

    /* --- The Asynchronous Observer Pipeline --- */
    public void placeOrder(Order order) {
        orders.put(order.getId(), order);
        
        // SDE3: Dispatch background thread instantly. The UI terminal DOES NOT freeze here!
        dispatcher.dispatchKitchenNotification(order);
        System.out.println("POS: Order " + order.getId() + " submitted immediately and handed to event router.");
    }

    public void updateOrderStatus(int orderId, OrderStatus status) {
        Order order = orders.get(orderId);
        if (order != null) {
            order.setStatus(status);
            
            // Re-execute observer dispatch structurally!
            dispatcher.dispatchStaffNotification(order);
        }
    }
    
    public void shutdownDispatcher() {
        dispatcher.shutdown();
    }
}
