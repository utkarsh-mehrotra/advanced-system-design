package parkinglot_sde3;

import parkinglot_sde3.exception.InvalidTicketException;
import parkinglot_sde3.exception.ParkingLotFullException;
import parkinglot_sde3.strategy.ParkingStrategy;
import parkinglot_sde3.strategy.PricingStrategy;
import parkinglot_sde3.vehicletype.Vehicle;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Acts as the entry point API (Orchestrator) for the system.
 * Eliminates the Singleton anti-pattern by taking strategy dependencies.
 */
public class ParkingLotFacade {
    private final List<Level> levels;
    private final ParkingStrategy parkingStrategy;
    private final PricingStrategy pricingStrategy;
    
    // Centralized ticket storage for validation at exit
    private final Map<String, ParkingTicket> activeTickets;

    public ParkingLotFacade(ParkingStrategy parkingStrategy, PricingStrategy pricingStrategy) {
        this.levels = new ArrayList<>();
        this.parkingStrategy = parkingStrategy;
        this.pricingStrategy = pricingStrategy;
        this.activeTickets = new ConcurrentHashMap<>();
    }

    public void addLevel(Level level) {
        levels.add(level);
    }

    /**
     * Simulates a vehicle pulling up to the ticket dispenser.
     */
    public ParkingTicket entryGate(Vehicle vehicle) {
        Optional<ParkingSpot> spotOpt = parkingStrategy.findAndReserveSpot(levels, vehicle.getType(), vehicle.getLicensePlate());
        
        if (spotOpt.isPresent()) {
            ParkingSpot reservedSpot = spotOpt.get();
            // In a real system we'd find the associated level for the ticket, 
            // but for simplicity we'll just track the spot.
            Level assignedLevel = levels.stream()
                .filter(l -> l.getParkingSpots().contains(reservedSpot))
                .findFirst()
                .orElse(null);

            String ticketId = "TKT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            ParkingTicket ticket = new ParkingTicket(ticketId, vehicle.getLicensePlate(), reservedSpot, assignedLevel);
            
            activeTickets.put(ticketId, ticket);
            System.out.println("ENTRY: Ticket " + ticketId + " dispensed for " + vehicle.getLicensePlate() + 
                               ". Spot Reserved: Floor " + (assignedLevel != null ? assignedLevel.getFloor() : "?") + 
                               ", Spot " + reservedSpot.getSpotNumber());
            return ticket;
        } else {
            throw new ParkingLotFullException("Parking lot is fully occupied for vehicle type: " + vehicle.getType());
        }
    }

    /**
     * Computes the price, pays it, and unparks securely.
     */
    public BigDecimal exitGate(String ticketId) {
        ParkingTicket ticket = activeTickets.get(ticketId);
        if (ticket == null || ticket.isPaid()) {
            throw new InvalidTicketException("Ticket is invalid, not found, or already processed.");
        }

        BigDecimal cost = pricingStrategy.calculateCost(ticket);
        
        // Process Payment (Simulated)
        System.out.println("EXIT: Processing payment of $" + cost + " for ticket " + ticketId);
        ticket.markPaid();

        // Release the spot back to inventory
        ticket.getAllocatedSpot().unpark();
        activeTickets.remove(ticketId);

        System.out.println("EXIT: Gate Opened. Spot " + ticket.getAllocatedSpot().getSpotNumber() + " is now free.");
        return cost;
    }

    public void displayAvailability() {
        System.out.println("\n--- Current Lot Availability ---");
        for (Level level : levels) {
            level.displayAvailability();
        }
    }
}
