package lrucache_sde3;

public class CacheEvictedLogger {
    public CacheEvictedLogger() {
        EventBus.getInstance().subscribe("CACHE_EVICTION", this::logEviction);
    }

    private void logEviction(Object payload) {
        System.out.println("Analytics [Async Worker]: " + payload);
    }
}
