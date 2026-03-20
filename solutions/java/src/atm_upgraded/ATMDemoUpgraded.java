package atm_upgraded;

import java.math.BigDecimal;

public class ATMDemoUpgraded {

    public static void main(String[] args) {
        BankingService bankingService = new BankingService();
        // SDE3: Creating the immutable BigDecimal ledger
        Account myAccount = new Account("12345678", new BigDecimal("5000.00"));
        bankingService.createAccount(myAccount);

        // Load machine with $10,000 cash
        CashDispenser dispenser = new CashDispenser(10000);
        ATM atm = new ATM(bankingService, dispenser);

        Card myCard = new Card("1111-2222-3333-4444", "1234", "12345678");

        System.out.println("--- Procedural Block Testing ---");
        // Attempting to withdraw cash blindly WITHOUT inserting a card
        // Proves the GoF State Pattern prevents SDE1 procedural hacking
        atm.withdrawCash(new BigDecimal("100.00")); 
        
        System.out.println("\\n--- Valid Stateful Transaction ---");
        atm.insertCard(myCard);
        atm.enterPin("1234");
        atm.withdrawCash(new BigDecimal("500.00"));

        System.out.println("\\n--- Mechanical Hardware Failure Saga Test ---");
        // We will loop transactions until the 10% random mechanical jam simulator fires.
        // We evaluate whether the user's funds were safely rebated back!

        for (int i = 0; i < 20; i++) {
            System.out.println("--- Tx " + (i+1) + " ---");
            atm.insertCard(myCard);
            atm.enterPin("1234");
            atm.withdrawCash(new BigDecimal("500.00"));
            
            // Just verifying balance safely to ensure we didn't hemorrhage funds silently
            System.out.println("Verified Balance Remainder: $" + myAccount.getBalance());
            if (myAccount.getBalance().compareTo(new BigDecimal("5000.00")) == 0) {
                System.out.println("SAGA VERIFIED: Money was fully returned due to hardware failure!");
                break;
            } else if (i > 15) {
                // To avoid infinite loops just in case random generator misses
                System.out.println("Hardware survived the load test cleanly. Test manual interrupt.");
                break;
            }
        }
    }
}
