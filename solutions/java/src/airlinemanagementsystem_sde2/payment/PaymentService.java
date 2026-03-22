package airlinemanagementsystem_sde2.payment;

public interface PaymentService {
    /**
     * Processes a payment.
     * @param payment The payment details
     * @return true if successful, false otherwise
     */
    boolean processPayment(Payment payment);
}
