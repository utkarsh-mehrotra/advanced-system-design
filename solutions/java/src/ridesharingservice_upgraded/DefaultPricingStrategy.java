package ridesharingservice_upgraded;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DefaultPricingStrategy implements PricingStrategy {
    private static final BigDecimal BASE_FARE = new BigDecimal("2.00");
    private static final BigDecimal PER_KM_RATE = new BigDecimal("1.50");
    private static final BigDecimal PER_MIN_RATE = new BigDecimal("0.25");

    @Override
    public BigDecimal calculateFare(double distanceKm, double durationMinutes) {
        BigDecimal distanceCost = PER_KM_RATE.multiply(BigDecimal.valueOf(distanceKm));
        BigDecimal timeCost = PER_MIN_RATE.multiply(BigDecimal.valueOf(durationMinutes));
        return BASE_FARE.add(distanceCost).add(timeCost).setScale(2, RoundingMode.HALF_UP);
    }
}
