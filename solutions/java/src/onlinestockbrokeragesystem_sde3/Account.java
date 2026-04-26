package onlinestockbrokeragesystem_sde3;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Account {
    private final String id;
    private final User user;
    private BigDecimal balance;
    private final Lock lock = new ReentrantLock();

    public Account(String id, User user, BigDecimal initialBalance) {
        this.id = id;
        this.user = user;
        this.balance = initialBalance.setScale(2, RoundingMode.HALF_UP);
    }

    /** Returns true if funds were successfully deducted. */
    public boolean debitFunds(BigDecimal amount) {
        lock.lock();
        try {
            if (balance.compareTo(amount) < 0) return false;
            balance = balance.subtract(amount).setScale(2, RoundingMode.HALF_UP);
            return true;
        } finally {
            lock.unlock();
        }
    }

    public void creditFunds(BigDecimal amount) {
        lock.lock();
        try {
            balance = balance.add(amount).setScale(2, RoundingMode.HALF_UP);
        } finally {
            lock.unlock();
        }
    }

    public String getId() { return id; }
    public User getUser() { return user; }
    public BigDecimal getBalance() { lock.lock(); try { return balance; } finally { lock.unlock(); } }
}
