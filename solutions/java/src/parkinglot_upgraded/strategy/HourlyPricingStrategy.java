package parkinglot_upgraded.strategy;

import parkinglot_upgraded.ParkingTicket;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;

public class HourlyPricingStrategy implements PricingStrategy {
    private final BigDecimal hourlyRate;

    public HourlyPricingStrategy(BigDecimal hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    @Override
    public BigDecimal calculateCost(ParkingTicket ticket) {
        LocalDateTime exitTime = LocalDateTime.now();
        Duration duration = Duration.between(ticket.getEntryTime(), exitTime);
        long minutes = duration.toMinutes();

        // If parked for less than 15 minutes, it's free!
        if (minutes <= 15) {
            return BigDecimal.ZERO;
        }

        // Calculate hours rounded up
        long hours = (long) Math.ceil(minutes / 60.0);
        if (hours == 0) hours = 1;

        return hourlyRate.multiply(BigDecimal.valueOf(hours)).setScale(2, RoundingMode.HALF_UP);
    }
}
