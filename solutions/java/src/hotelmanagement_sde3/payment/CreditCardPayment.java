package hotelmanagement_sde3.payment;

import java.math.BigDecimal;

public class CreditCardPayment implements Payment {
    @Override
    public boolean processPayment(BigDecimal amount) {
        System.out.println("Processing credit card payment of $" + amount);
        return true;
    }
}
