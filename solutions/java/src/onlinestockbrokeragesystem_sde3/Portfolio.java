package onlinestockbrokeragesystem_sde3;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/** Thread-safe portfolio tracking positions per stock symbol. */
public class Portfolio {
    private final ConcurrentHashMap<String, Integer> holdings = new ConcurrentHashMap<>();
    private final Lock lock = new ReentrantLock();

    public void addPosition(String symbol, int quantity) {
        holdings.merge(symbol, quantity, Integer::sum);
    }

    /** Returns true if position was successfully reduced. */
    public boolean removePosition(String symbol, int quantity) {
        lock.lock();
        try {
            int current = holdings.getOrDefault(symbol, 0);
            if (current < quantity) return false;
            if (current == quantity) holdings.remove(symbol);
            else holdings.put(symbol, current - quantity);
            return true;
        } finally {
            lock.unlock();
        }
    }

    public int getHoldings(String symbol) {
        return holdings.getOrDefault(symbol, 0);
    }
}
