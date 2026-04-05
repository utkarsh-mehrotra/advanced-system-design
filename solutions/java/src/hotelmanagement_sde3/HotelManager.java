package hotelmanagement_sde3;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HotelManager {
    private final Map<String, Room> inventory = new ConcurrentHashMap<>();

    public void addRoom(Room r) {
        inventory.put(r.getRoomId(), r);
    }

    public boolean reserveRoom(String roomId, String userId, int days) {
        Room r = inventory.get(roomId);
        if (r != null && r.bookRoom()) { // Atomic CAS lock
            System.out.println("HotelManager: Room " + roomId + " secured lock-free for " + userId);
            
            double totalCost = r.getCostPerNight() * days;
            // Initiate heavy billing asynchronously
            EventBus.getInstance().publish("GENERATE_BILL", userId + ":" + roomId + ":" + totalCost);
            return true;
        }
        return false;
    }
}
