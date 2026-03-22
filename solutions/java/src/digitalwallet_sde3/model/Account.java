package digitalwallet_sde3.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Account State Model in the Distributed Ledger.
 */
public class Account {
    private final String accountId;
    private BigDecimal availableBalance;
    private BigDecimal lockedBalance; // Held during active distributed transactions (SMC processing)
    
    // Per-account optimistic/pessimistic lock (Never locked concurrently with another account!)
    private final Lock lock = new ReentrantLock();

    public Account(String accountId, BigDecimal initialBalance) {
        this.accountId = accountId;
        this.availableBalance = initialBalance.setScale(2, RoundingMode.HALF_UP);
        this.lockedBalance = BigDecimal.ZERO;
    }

    public boolean reserveFunds(BigDecimal amount) {
        lock.lock();
        try {
            if (availableBalance.compareTo(amount) < 0) return false;
            availableBalance = availableBalance.subtract(amount);
            lockedBalance = lockedBalance.add(amount);
            return true;
        } finally {
            lock.unlock();
        }
    }

    public void creditFunds(BigDecimal amount) {
        lock.lock();
        try {
            availableBalance = availableBalance.add(amount).setScale(2, RoundingMode.HALF_UP);
        } finally {
            lock.unlock();
        }
    }

    public void commitReservedFunds(BigDecimal amount) {
        lock.lock();
        try {
            lockedBalance = lockedBalance.subtract(amount);
        } finally {
            lock.unlock();
        }
    }

    public void rollbackReservedFunds(BigDecimal amount) {
        lock.lock();
        try {
            lockedBalance = lockedBalance.subtract(amount);
            availableBalance = availableBalance.add(amount);
        } finally {
            lock.unlock();
        }
    }

    public String getAccountId() { return accountId; }
    
    public BigDecimal getAvailableBalance() { 
        lock.lock(); 
        try { return availableBalance; } finally { lock.unlock(); } 
    }
}
