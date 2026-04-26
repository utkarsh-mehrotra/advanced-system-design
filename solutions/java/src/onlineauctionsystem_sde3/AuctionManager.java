package onlineauctionsystem_sde3;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class AuctionManager {
    private final Map<String, AuctionItem> items = new ConcurrentHashMap<>();
    // Fine-grained locks per item
    private final Map<String, ReentrantLock> itemLocks = new ConcurrentHashMap<>();
    private final List<AuctionObserver> observers = new CopyOnWriteArrayList<>();

    public void addObserver(AuctionObserver observer) {
        observers.add(observer);
    }

    public void addItem(AuctionItem item) {
        items.put(item.getItemId(), item);
        itemLocks.put(item.getItemId(), new ReentrantLock());
    }

    public boolean placeBid(String itemId, String userId, double amount) {
        AuctionItem item = items.get(itemId);
        if (item == null || !item.isActive()) return false;

        ReentrantLock lock = itemLocks.get(itemId);
        lock.lock();
        try {
            if (item.isActive() && amount > item.getCurrentHighestBid()) {
                item.setCurrentHighestBid(amount);
                item.setHighestBidderId(userId);
                
                notifyNewBid(itemId, amount, userId);
                return true;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

    private void notifyNewBid(String itemId, double amount, String userId) {
        for (AuctionObserver obs : observers) {
            obs.onNewHighestBid(itemId, amount, userId);
        }
    }
}
