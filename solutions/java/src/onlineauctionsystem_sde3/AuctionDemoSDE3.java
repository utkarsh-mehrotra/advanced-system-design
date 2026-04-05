package onlineauctionsystem_sde3;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AuctionDemoSDE3 {
    public static void main(String[] args) throws InterruptedException {
        // Consumer listening to async bid stream
        EventBus.getInstance().subscribe("BID_ACCEPTED", payload -> {
            System.out.println("WEBSOCKET BROADCAST: " + payload);
        });

        BidProcessor processor = new BidProcessor();
        processor.registerItem(new AuctionItem("RARE_COIN", 500));

        // High Concurrency Test
        ExecutorService executor = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(10);

        for (int i = 0; i < 10; i++) {
            final int bidAmount = 500 + (10 * i);
            executor.submit(() -> {
                processor.submitBidAsync("RARE_COIN", "User_" + Thread.currentThread().getId(), bidAmount);
                latch.countDown();
            });
        }
        
        latch.await();
        executor.shutdown();
        System.out.println("All concurrent bids processed via lock-free CAS.");
    }
}
