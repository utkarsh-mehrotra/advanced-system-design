package onlinestockbrokeragesystem_sde3.model;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Tracks stock holdings per account.
 * Includes 'locked' positions to prevent double-spending when active sell orders are placed.
 */
public class Portfolio {
    private final String accountId;
    private final ConcurrentHashMap<String, Position> positions = new ConcurrentHashMap<>();
    private final Lock lock = new ReentrantLock();

    public Portfolio(String accountId) {
        this.accountId = accountId;
    }

    private static class Position {
        int available;
        int locked;
        Position(int available) { this.available = available; this.locked = 0; }
    }

    public void addPosition(String symbol, int quantity) {
        lock.lock();
        try {
            positions.compute(symbol, (k, v) -> {
                if (v == null) return new Position(quantity);
                v.available += quantity;
                return v;
            });
        } finally {
            lock.unlock();
        }
    }

    public boolean reservePosition(String symbol, int quantity) {
        lock.lock();
        try {
            Position pos = positions.get(symbol);
            if (pos == null || pos.available < quantity) return false;
            pos.available -= quantity;
            pos.locked += quantity;
            return true;
        } finally {
            lock.unlock();
        }
    }

    public void commitReservedPosition(String symbol, int quantity) {
        lock.lock();
        try {
            Position pos = positions.get(symbol);
            if (pos != null) {
                pos.locked -= quantity;
            }
        } finally {
            lock.unlock();
        }
    }

    public void releaseReservedPosition(String symbol, int quantity) {
        lock.lock();
        try {
            Position pos = positions.get(symbol);
            if (pos != null) {
                pos.locked -= quantity;
                pos.available += quantity;
            }
        } finally {
            lock.unlock();
        }
    }

    public int getAvailableHoldings(String symbol) {
        lock.lock();
        try {
            Position pos = positions.get(symbol);
            return pos == null ? 0 : pos.available;
        } finally {
            lock.unlock();
        }
    }
}
