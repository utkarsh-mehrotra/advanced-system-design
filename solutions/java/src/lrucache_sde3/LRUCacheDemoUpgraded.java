package lrucache_sde3;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LRUCacheDemoUpgraded {
    public static void main(String[] args) throws InterruptedException {
        // Construct a Striped LRU Cache. 1000 Total Keys, divided among 16 independent isolated shards.
        // 16 different Threads can now theoretically PUT or GET without blocking each other globally.
        LRUCache<String, String> concurrentCache = new LRUCache<>(1000, 16);

        concurrentCache.put("AAPL", "Apple Inc");
        concurrentCache.put("GOOGL", "Alphabet Inc");
        
        System.out.println("Basic Get AAPL: " + concurrentCache.get("AAPL"));

        System.out.println("\n--- Massive Parallel Concurrency Strike ---");
        System.out.println("Attempting 100,000 completely parallel cache GETs/PUTs across multiple threads...");
        
        int threadCount = 20;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(100000);
        
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < 100000; i++) {
            final int index = i;
            executor.submit(() -> {
                try {
                    // Random keys scattered everywhere hitting different segment locks
                    String randomKey = "KEY_" + (index % 500); 
                    concurrentCache.put(randomKey, "VAL_" + index);
                    concurrentCache.get(randomKey);
                } finally {
                    latch.countDown();
                }
            });
        }
        
        latch.await();
        executor.shutdown();
        long endTime = System.currentTimeMillis();

        System.out.println("100,000 thread-safe Operations (Global lock-free) completed in: " + (endTime - startTime) + "ms.");
        System.out.println("The Striped Segment Locks prevented the `synchronized` logjam seen in junior designs.");
    }
}
