package atm_sde3;

import java.math.BigDecimal;

public class AtmDemoSDE3 {
    public static void main(String[] args) {
        System.out.println("--- Booting ATM SDE3 (Lock-Free Event-Driven) ---");

        Account premiumAccount = new Account("1001", 2000.00);
        AtmController controller = new AtmController();
        
        Card card = new Card("1111-2222-3333-4444", "1234", "1001");
        
        // Testing CAS-based state transitions
        controller.insertCard(card);
        controller.enterPin("1234");
        controller.processWithdrawal(premiumAccount, new BigDecimal("500.00"));

        System.out.println("Remaining Balance (Optimistic Read): $" + premiumAccount.getBalance());
    }
}
