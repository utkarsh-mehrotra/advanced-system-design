package ridesharingservice_sde3;

import java.math.BigDecimal;

public interface PricingStrategy {
    BigDecimal calculateFare(double distanceKm, double durationMinutes);
}
