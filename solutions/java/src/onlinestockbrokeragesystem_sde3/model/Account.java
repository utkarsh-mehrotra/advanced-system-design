package onlinestockbrokeragesystem_sde3.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * User Account containing cash balance and locked balance (for open limit orders).
 */
public class Account {
    private final String id;
    private BigDecimal availableBalance;
    private BigDecimal lockedBalance;
    private final Lock lock = new ReentrantLock();

    public Account(String id, BigDecimal initialBalance) {
        this.id = id;
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

    public void releaseReservedFunds(BigDecimal amount) {
        lock.lock();
        try {
            lockedBalance = lockedBalance.subtract(amount);
            availableBalance = availableBalance.add(amount);
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

    public void creditFunds(BigDecimal amount) {
        lock.lock();
        try {
            availableBalance = availableBalance.add(amount).setScale(2, RoundingMode.HALF_UP);
        } finally {
            lock.unlock();
        }
    }

    public String getId() { return id; }
    
    public BigDecimal getAvailableBalance() { 
        lock.lock(); 
        try { return availableBalance; } finally { lock.unlock(); } 
    }
    
    public BigDecimal getLockedBalance() { 
        lock.lock(); 
        try { return lockedBalance; } finally { lock.unlock(); } 
    }
}
