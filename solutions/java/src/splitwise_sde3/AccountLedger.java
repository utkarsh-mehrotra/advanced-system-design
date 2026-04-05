package splitwise_sde3;

import java.util.concurrent.atomic.AtomicReference;

public class AccountLedger {
    private final String userId;
    // Positive means they are owed, negative means they owe
    private final AtomicReference<Double> netBalance;

    public AccountLedger(String userId) {
        this.userId = userId;
        this.netBalance = new AtomicReference<>(0.0);
    }

    public String getUserId() { return userId; }
    public double getBalance() { return netBalance.get(); }

    public void adjustBalance(double delta) {
        while (true) {
            Double current = netBalance.get();
            Double next = current + delta;
            if (netBalance.compareAndSet(current, next)) {
                return;
            }
        }
    }
}
