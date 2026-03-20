package ridesharingservice_upgraded;

import java.math.BigDecimal;

public interface PricingStrategy {
    BigDecimal calculateFare(double distanceKm, double durationMinutes);
}
