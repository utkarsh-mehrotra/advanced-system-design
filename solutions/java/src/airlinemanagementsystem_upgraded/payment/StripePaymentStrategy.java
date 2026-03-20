package airlinemanagementsystem_upgraded.payment;

import java.math.BigDecimal;
import java.util.UUID;

public class StripePaymentStrategy implements PaymentService {
    
    // Simulating Stripe payment behavior without requiring actual API keys for demo.
    @Override
    public boolean processPayment(Payment payment) {
        System.out.println("Processing payment " + payment.getPaymentId() + 
                           " via StripeStrategy for amount $" + payment.getAmount());

        // Simulating processing delay and some validation
        if (payment.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            payment.setStatus(PaymentStatus.FAILED);
            System.err.println("Invalid amount logic for Stripe.");
            return false;
        }

        try {
            Thread.sleep(200); // Simulate network latency
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Simulating success
        System.out.println("Stripe Payment Successful! Transaction Ref: " + UUID.randomUUID().toString());
        payment.setStatus(PaymentStatus.COMPLETED);
        return true;
    }
}
