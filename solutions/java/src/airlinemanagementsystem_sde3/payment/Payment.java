package airlinemanagementsystem_sde3.payment;

import java.math.BigDecimal;

public class Payment {
    private final String paymentId;
    private final String bookingId;
    private final BigDecimal amount;
    private PaymentStatus status;

    public Payment(String paymentId, String bookingId, BigDecimal amount) {
        this.paymentId = paymentId;
        this.bookingId = bookingId;
        this.amount = amount;
        this.status = PaymentStatus.PENDING;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public String getBookingId() {
        return bookingId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public synchronized PaymentStatus getStatus() {
        return status;
    }

    public synchronized void setStatus(PaymentStatus status) {
        this.status = status;
    }
}
