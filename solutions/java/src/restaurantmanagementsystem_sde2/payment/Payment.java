package restaurantmanagementsystem_sde2.payment;

import java.math.BigDecimal;

public interface Payment {
    boolean processPayment(BigDecimal amount);
}
