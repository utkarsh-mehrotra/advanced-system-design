package restaurantmanagementsystem_upgraded.payment;

import java.math.BigDecimal;

public interface Payment {
    boolean processPayment(BigDecimal amount);
}
