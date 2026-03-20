package lrucache_upgraded;

/**
 * SDE3/FAANG Concurrent LRU Cache adopting the Lock-Striping technique.
 * Instantiates N Independent Segments. Routinely achieves near-limitless parallel throughput
 * by ensuring parallel requests to separate Keys hit completely different Lock instances.
 */
public class LRUCache<K, V> {
    private final LRUSegment<K, V>[] segments;
    private final int segmentCount;

    @SuppressWarnings("unchecked")
    public LRUCache(int totalCapacity, int segmentCount) {
        // Must be > 0. Power of 2 (16, 32, 64) is conventional for bitwise modulo, but standard Math.abs modulo is fine here
        this.segmentCount = segmentCount;
        this.segments = new LRUSegment[segmentCount];
        
        int capacityPerSegment = (int) Math.ceil((double) totalCapacity / segmentCount);
        
        for (int i = 0; i < segmentCount; i++) {
            segments[i] = new LRUSegment<>(capacityPerSegment);
        }
    }

    // Hash router determining the absolute sub-segment this Key is assigned to.
    private LRUSegment<K, V> getSegment(K key) {
        int hash = key.hashCode();
        if (hash == Integer.MIN_VALUE) { 
            hash = 0; 
        }
        int index = Math.abs(hash) % segmentCount;
        return segments[index];
    }

    public V get(K key) {
        return getSegment(key).get(key);
    }

    public void put(K key, V value) {
        getSegment(key).put(key, value);
    }
}
