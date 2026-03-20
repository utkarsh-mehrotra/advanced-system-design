package airlinemanagementsystem.payment;

public class PaymentProcessor {
    private static PaymentProcessor instance;

    private PaymentService paymentService;

    // Default to Stripe
    private PaymentProcessor() {
        this.paymentService = new StripePaymentService();
    }

    public static synchronized PaymentProcessor getInstance() {
        if (instance == null) {
            instance = new PaymentProcessor();
        }
        return instance;
    }

    public void setPaymentService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public void processPayment(Payment payment) {
        paymentService.processPayment(payment);
    }
}
