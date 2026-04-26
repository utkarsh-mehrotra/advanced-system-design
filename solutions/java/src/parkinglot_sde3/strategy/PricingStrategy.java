package parkinglot_sde3.strategy;

import parkinglot_sde3.ParkingTicket;
import java.math.BigDecimal;

public interface PricingStrategy {
    /**
     * Calculates the parking fee based on the ticket.
     * @param ticket The ticket used at entry
     * @return The cost as a precise BigDecimal
     */
    BigDecimal calculateCost(ParkingTicket ticket);
}
