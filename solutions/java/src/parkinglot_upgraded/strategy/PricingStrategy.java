package parkinglot_upgraded.strategy;

import parkinglot_upgraded.ParkingTicket;
import java.math.BigDecimal;

public interface PricingStrategy {
    /**
     * Calculates the parking fee based on the ticket.
     * @param ticket The ticket used at entry
     * @return The cost as a precise BigDecimal
     */
    BigDecimal calculateCost(ParkingTicket ticket);
}
