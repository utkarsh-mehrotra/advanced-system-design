package splitwise_sde3;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ExpenseManager {
    private final Map<String, AccountLedger> ledgers = new ConcurrentHashMap<>();

    public void registerUser(AccountLedger ledger) {
        ledgers.put(ledger.getUserId(), ledger);
    }

    public void addExpense(String paidBy, String splitWith, double totalAmount) {
        AccountLedger payer = ledgers.get(paidBy);
        AccountLedger owee = ledgers.get(splitWith);

        if (payer != null && owee != null) {
            double splitAmount = totalAmount / 2.0;

            // Atomic independent adjustments
            payer.adjustBalance(splitAmount); 
            owee.adjustBalance(-splitAmount);

            System.out.println("ExpenseManager: Processed atomic ledger split.");
            
            // Send asynchronous notification
            EventBus.getInstance().publish("BALANCE_UPDATED", owee.getUserId() + " owes " + splitAmount);
        }
    }
}
