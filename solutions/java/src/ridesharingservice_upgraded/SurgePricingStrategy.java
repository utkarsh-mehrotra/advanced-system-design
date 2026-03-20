package ridesharingservice_upgraded;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Demonstrates Strategy extensibility — injected at runtime during peak hours.
 */
public class SurgePricingStrategy implements PricingStrategy {
    private final double surgeMultiplier;
    private final DefaultPricingStrategy base = new DefaultPricingStrategy();

    public SurgePricingStrategy(double surgeMultiplier) {
        this.surgeMultiplier = surgeMultiplier;
    }

    @Override
    public BigDecimal calculateFare(double distanceKm, double durationMinutes) {
        BigDecimal baseFare = base.calculateFare(distanceKm, durationMinutes);
        return baseFare.multiply(BigDecimal.valueOf(surgeMultiplier)).setScale(2, RoundingMode.HALF_UP);
    }
}
