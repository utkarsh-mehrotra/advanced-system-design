package onlineauctionsystem_sde3;

public class AuctionItem {
    private final String itemId;
    private final double startingPrice;
    private double currentHighestBid;
    private String highestBidderId;
    private boolean isActive;

    public AuctionItem(String itemId, double startingPrice) {
        this.itemId = itemId;
        this.startingPrice = startingPrice;
        this.currentHighestBid = startingPrice;
        this.highestBidderId = null;
        this.isActive = true;
    }

    public String getItemId() { return itemId; }
    public double getCurrentHighestBid() { return currentHighestBid; }
    public String getHighestBidderId() { return highestBidderId; }
    public boolean isActive() { return isActive; }

    void setCurrentHighestBid(double currentHighestBid) { this.currentHighestBid = currentHighestBid; }
    void setHighestBidderId(String highestBidderId) { this.highestBidderId = highestBidderId; }
    void setActive(boolean active) { isActive = active; }
}
