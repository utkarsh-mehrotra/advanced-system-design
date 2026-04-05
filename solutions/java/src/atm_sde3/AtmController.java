package atm_sde3;

public class AtmController {
    public void processWithdrawal(Account account, double amount) {
        System.out.println("AtmController: Initiating withdrawal sequence for $" + amount);
        
        if (account.withdraw(amount)) {
            System.out.println("AtmController: Ledger debited successfully.");
            // Decouple physical dispensing from ledger writing
            EventBus.getInstance().publish("DISPENSE_CASH", amount);
            
            // Decouple auditing
            EventBus.getInstance().publish("AUDIT_LOG", "Withdrawal:" + account.getAccountNumber() + ":" + amount);
        } else {
            System.out.println("AtmController: Verification failed (Insufficient Funds).");
        }
    }
}
