package carrentalsystem_sde2;

public interface PricingStrategy {
    double calculatePrice(Car car, int days);
}

class StandardPricingStrategy implements PricingStrategy {
    private static final double BASE_RATE = 50.0;
    
    @Override
    public double calculatePrice(Car car, int days) {
        return BASE_RATE * days;
    }
}

class PremiumPricingStrategy implements PricingStrategy {
    private static final double BASE_RATE = 100.0;
    
    @Override
    public double calculatePrice(Car car, int days) {
        return (BASE_RATE * days) * 0.9; // 10% discount for premium
    }
}
