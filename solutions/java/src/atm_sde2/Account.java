package atm_sde2;

import java.math.BigDecimal;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Account {
    private final String accountNumber;
    
    // SDE3: Critical migration from double to BigDecimal for exact cent accuracy
    private BigDecimal balance;
    
    // SDE3: Granular Lock protecting mutations on this account without blocking global banking limits
    private final Lock lock;

    public Account(String accountNumber, BigDecimal initialBalance) {
        this.accountNumber = accountNumber;
        this.balance = initialBalance;
        this.lock = new ReentrantLock();
    }

    public String getAccountNumber() { return accountNumber; }

    public BigDecimal getBalance() {
        lock.lock();
        try {
            return balance;
        } finally {
            lock.unlock();
        }
    }

    public void debit(BigDecimal amount) {
        lock.lock();
        try {
            if (balance.compareTo(amount) >= 0) {
                balance = balance.subtract(amount);
            } else {
                throw new IllegalArgumentException("Insufficient funds in account.");
            }
        } finally {
            lock.unlock();
        }
    }

    public void credit(BigDecimal amount) {
        lock.lock();
        try {
            balance = balance.add(amount);
        } finally {
            lock.unlock();
        }
    }
}
