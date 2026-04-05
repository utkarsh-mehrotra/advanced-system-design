package onlineauctionsystem_sde3;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BidProcessor {
    private final Map<String, AuctionItem> items = new ConcurrentHashMap<>();

    public void registerItem(AuctionItem item) {
        items.put(item.getItemId(), item);
    }

    public void submitBidAsync(String itemId, String userId, double amount) {
        AuctionItem item = items.get(itemId);
        if (item != null) {
            boolean success = item.placeBid(userId, amount);
            if (success) {
                // Fan-out to websockets, notifications, analytics asynchronously
                EventBus.getInstance().publish("BID_ACCEPTED", itemId + ":" + amount + ":" + userId);
            }
        }
    }
}
