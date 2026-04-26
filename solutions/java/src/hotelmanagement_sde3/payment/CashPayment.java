package hotelmanagement_sde3.payment;

import java.math.BigDecimal;

public class CashPayment implements Payment {
    @Override
    public boolean processPayment(BigDecimal amount) {
        System.out.println("Processing cash payment of $" + amount);
        return true;
    }
}
