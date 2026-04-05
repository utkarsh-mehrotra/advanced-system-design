package hotelmanagement_sde3;

import java.util.concurrent.atomic.AtomicReference;

public class Room {
    private final String roomId;
    private final double costPerNight;
    private final AtomicReference<RoomStatus> status;

    public Room(String roomId, double costPerNight) {
        this.roomId = roomId;
        this.costPerNight = costPerNight;
        this.status = new AtomicReference<>(RoomStatus.AVAILABLE);
    }

    public String getRoomId() { return roomId; }
    public double getCostPerNight() { return costPerNight; }

    public boolean bookRoom() {
        return status.compareAndSet(RoomStatus.AVAILABLE, RoomStatus.BOOKED);
    }
    
    public boolean checkOut() {
        return status.compareAndSet(RoomStatus.OCCUPIED, RoomStatus.MAINTENANCE);
    }
}
