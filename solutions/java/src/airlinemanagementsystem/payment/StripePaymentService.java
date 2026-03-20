package airlinemanagementsystem.payment;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

public class StripePaymentService implements PaymentService {

    public StripePaymentService() {
        // Retrieve API key from environment variable
        String apiKey = System.getenv("STRIPE_API_KEY");
        if (apiKey == null || apiKey.isEmpty()) {
            System.err.println("WARNING: STRIPE_API_KEY environment variable not found. Transactions will fail.");
        }
        Stripe.apiKey = apiKey;
    }

    @Override
    public boolean processPayment(Payment payment) {
        System.out.println("Processing payment of $" + payment.getAmount() + " via Stripe SDK...");

        try {
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount((long) (payment.getAmount() * 100)) // Amount in cents
                    .setCurrency("usd")
                    .setDescription("Payment for Booking ID: " + payment.getPaymentId())
                    // In a real app, you'd add a PaymentMethod ID here
                    .setPaymentMethod("pm_card_visa") // Test card token
                    .setConfirm(true) // Automatically confirm the payment
                    .setReturnUrl("https://example.com/return") // Required for automatic confirmation
                    .build();

            PaymentIntent paymentIntent = PaymentIntent.create(params);

            System.out.println("Stripe Payment Successful! Intent ID: " + paymentIntent.getId());
            payment.setStatus(PaymentStatus.COMPLETED);
            return true;

        } catch (StripeException e) {
            System.err.println("Stripe Payment Failed: " + e.getMessage());
            payment.setStatus(PaymentStatus.FAILED);
            return false;
        }
    }
}
