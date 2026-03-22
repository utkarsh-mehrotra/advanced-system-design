package ridesharingservice_sde2;

import java.math.BigDecimal;

public interface PricingStrategy {
    BigDecimal calculateFare(double distanceKm, double durationMinutes);
}
