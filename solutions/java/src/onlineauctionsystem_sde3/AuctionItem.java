package onlineauctionsystem_sde3;

import java.util.concurrent.atomic.AtomicReference;

public class AuctionItem {
    private final String itemId;
    // Atomic reference holds immutable state class for thread-safe compare-and-swap
    private final AtomicReference<BidState> bidState;
    private final AtomicReference<AuctionState> status;

    public static class BidState {
        public final double amount;
        public final String bidderId;
        public BidState(double amount, String bidderId) {
            this.amount = amount;
            this.bidderId = bidderId;
        }
    }

    public AuctionItem(String itemId, double startingPrice) {
        this.itemId = itemId;
        this.bidState = new AtomicReference<>(new BidState(startingPrice, null));
        this.status = new AtomicReference<>(AuctionState.LIVE);
    }

    public String getItemId() { return itemId; }
    public AuctionState getStatus() { return status.get(); }
    public BidState getCurrentBid() { return bidState.get(); }

    // Lock-free highly concurrent bid placement using CAS (Optimistic Locking)
    public boolean placeBid(String bidderId, double amount) {
        if (status.get() != AuctionState.LIVE) return false;

        while (true) {
            BidState current = bidState.get();
            if (amount <= current.amount) {
                return false; // Bid too low
            }
            BidState next = new BidState(amount, bidderId);
            if (bidState.compareAndSet(current, next)) {
                return true; // Successfully swapped state!
            }
            // If compareAndSet fails, another thread updated bidState. The loop retries!
        }
    }
}
