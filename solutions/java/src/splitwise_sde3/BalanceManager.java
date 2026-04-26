package splitwise_sde3;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Thread-safe Central Ledger. 
 * Replaces the buggy internal ConcurrentHashMap inside User entities.
 */
public class BalanceManager {
    
    // Key format: "userA_id:userB_id". Maps to the amount userA owes userB.
    private final ConcurrentHashMap<String, BigDecimal> balances;

    public BalanceManager() {
        this.balances = new ConcurrentHashMap<>();
    }

    /**
     * Atomically adds an amount to the balance user1 owes user2.
     * @param user1 The user who owes
     * @param user2 The user who is owed
     * @param amount The precise amount
     */
    public void addOwedAmount(String user1, String user2, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) == 0) return;

        String key = getBalanceKey(user1, user2);
        
        // SDE3: `compute` is ATOMIC in ConcurrentHashMap. 
        // Eliminates the Time-Of-Check to Time-Of-Use race condition in the previous version!
        balances.compute(key, (k, currentBal) -> 
            (currentBal == null) ? amount : currentBal.add(amount)
        );
        
        // Reflect identically in the inverse relationship format (User2 owes User1 a negative amount)
        String inverseKey = getBalanceKey(user2, user1);
        balances.compute(inverseKey, (k, currentBal) -> 
            (currentBal == null) ? amount.negate() : currentBal.subtract(amount)
        );
    }

    public BigDecimal getBalance(String user1, String user2) {
        return balances.getOrDefault(getBalanceKey(user1, user2), BigDecimal.ZERO);
    }
    
    public void settleUp(String user1, String user2) {
        String key = getBalanceKey(user1, user2);
        String invKey = getBalanceKey(user2, user1);

        // Atomically zero out balances
        balances.put(key, BigDecimal.ZERO);
        balances.put(invKey, BigDecimal.ZERO);
    }

    private String getBalanceKey(String userId1, String userId2) {
        return userId1 + "::" + userId2;
    }

    // For Demo visualization
    public void printBalances() {
        System.out.println("--- System Balances Ledger ---");
        balances.forEach((key, b) -> {
            // Only print positive directions to avoid spam
            if (b.compareTo(BigDecimal.ZERO) > 0) {
                String[] parts = key.split("::");
                System.out.println("User " + parts[0] + " owes User " + parts[1] + " : $" + b);
            }
        });
        System.out.println("------------------------------");
    }
}
