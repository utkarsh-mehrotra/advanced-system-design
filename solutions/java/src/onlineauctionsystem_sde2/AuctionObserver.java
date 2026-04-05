package onlineauctionsystem_sde2;

public interface AuctionObserver {
    void onNewHighestBid(String itemId, double bidAmount, String bidderId);
    void onAuctionClosed(String itemId, String winnerId);
}

class BidNotifier implements AuctionObserver {
    @Override
    public void onNewHighestBid(String itemId, double bidAmount, String bidderId) {
        System.out.println("ALERT: New highest bid on " + itemId + " is $" + bidAmount + " by " + bidderId);
    }

    @Override
    public void onAuctionClosed(String itemId, String winnerId) {
        System.out.println("CLOSED: " + itemId + " won by " + winnerId);
    }
}
