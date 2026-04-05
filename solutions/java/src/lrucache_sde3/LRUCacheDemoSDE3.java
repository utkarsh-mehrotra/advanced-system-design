package lrucache_sde3;

public class LRUCacheDemoSDE3 {
    public static void main(String[] args) {
        new CacheEvictedLogger(); // Boot background analytics

        ConcurrentLRUCache<String, String> cache = new ConcurrentLRUCache<>(2);
        
        cache.put("1", "A");
        cache.put("2", "B");
        System.out.println("Fetching 1: " + cache.get("1")); // Pushes 1 to front
        
        System.out.println("Inserting 3 (triggers eviction of 2)...");
        cache.put("3", "C"); 
    }
}
