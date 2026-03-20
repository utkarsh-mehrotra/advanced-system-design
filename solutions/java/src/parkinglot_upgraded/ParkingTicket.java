package parkinglot_upgraded;

import java.time.LocalDateTime;

public class ParkingTicket {
    private final String ticketId;
    private final String licensePlate;
    private final ParkingSpot allocatedSpot;
    private final Level allocatedLevel;
    private final LocalDateTime entryTime;

    private boolean isPaid;

    public ParkingTicket(String ticketId, String licensePlate, ParkingSpot allocatedSpot, Level allocatedLevel) {
        this.ticketId = ticketId;
        this.licensePlate = licensePlate;
        this.allocatedSpot = allocatedSpot;
        this.allocatedLevel = allocatedLevel;
        this.entryTime = LocalDateTime.now();
        this.isPaid = false;
    }

    // Used for simulating time passage in demo
    public ParkingTicket(String ticketId, String licensePlate, ParkingSpot allocatedSpot, Level allocatedLevel, LocalDateTime entryTime) {
        this.ticketId = ticketId;
        this.licensePlate = licensePlate;
        this.allocatedSpot = allocatedSpot;
        this.allocatedLevel = allocatedLevel;
        this.entryTime = entryTime;
        this.isPaid = false;
    }

    public void markPaid() {
        this.isPaid = true;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public String getTicketId() {
        return ticketId;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public ParkingSpot getAllocatedSpot() {
        return allocatedSpot;
    }

    public Level getAllocatedLevel() {
        return allocatedLevel;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }
}
