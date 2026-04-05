package atm_sde3;

import java.util.concurrent.atomic.AtomicReference;

public class Account {
    private final String accountNumber;
    // Lock-free Double primitive requires wrapping in AtomicReference for CAS
    private final AtomicReference<Double> balance;

    public Account(String accountNumber, double initialBalance) {
        this.accountNumber = accountNumber;
        this.balance = new AtomicReference<>(initialBalance);
    }

    public String getAccountNumber() { return accountNumber; }
    public double getBalance() { return balance.get(); }

    public boolean withdraw(double amount) {
        while (true) {
            Double current = balance.get();
            if (current < amount) {
                return false; // Insufficient Funds
            }
            Double next = current - amount;
            if (balance.compareAndSet(current, next)) {
                return true;
            }
        }
    }
}
