package onlineshopping_sde3.payment;

public class CreditCardPayment implements Payment {
    private final boolean forceFailure;

    public CreditCardPayment() {
        this.forceFailure = false;
    }

    public CreditCardPayment(boolean forceFailure) {
        this.forceFailure = forceFailure;
    }

    @Override
    public boolean processPayment(double amount) {
        // Process credit card payment
        if (forceFailure) {
            System.out.println("[Payment Gateway] Credit card rejected for $" + amount);
            return false;
        }
        return true;
    }
}
