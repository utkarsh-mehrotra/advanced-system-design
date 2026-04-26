package onlineauctionsystem_sde3;

public class AuctionDemoSDE2 {
    public static void main(String[] args) {
        AuctionManager manager = new AuctionManager();
        manager.addObserver(new BidNotifier());

        manager.addItem(new AuctionItem("PAINTING_01", 100));
        
        System.out.println("Placing Bid $150...");
        manager.placeBid("PAINTING_01", "U1", 150);
        
        System.out.println("Placing Bid $120 (Should fail)...");
        manager.placeBid("PAINTING_01", "U2", 120);

        System.out.println("Placing Bid $200...");
        manager.placeBid("PAINTING_01", "U3", 200);
    }
}
