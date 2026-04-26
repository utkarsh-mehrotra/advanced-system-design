package restaurantmanagementsystem_sde3.payment;

import java.math.BigDecimal;

public interface Payment {
    boolean processPayment(BigDecimal amount);
}
