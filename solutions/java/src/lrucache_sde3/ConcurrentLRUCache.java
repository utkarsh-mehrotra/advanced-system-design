package lrucache_sde3;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ConcurrentLRUCache<K, V> {
    private final int capacity;
    private final Map<K, Node<K, V>> map;
    private final Node<K, V> head;
    private final Node<K, V> tail;
    
    // Protects only the doubly-linked list ordering, while the Map is lock-free concurrent
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();

    public ConcurrentLRUCache(int capacity) {
        this.capacity = capacity;
        this.map = new ConcurrentHashMap<>(capacity);
        this.head = new Node<>(null, null);
        this.tail = new Node<>(null, null);
        head.next = tail;
        tail.prev = head;
    }

    public V get(K key) {
        Node<K, V> node = map.get(key);
        if (node == null) return null;
        
        rwLock.writeLock().lock();
        try {
            removeNode(node);
            addNode(node);
        } finally {
            rwLock.writeLock().unlock();
        }
        return node.value;
    }

    public void put(K key, V value) {
        Node<K, V> node = map.get(key);
        
        rwLock.writeLock().lock();
        try {
            if (node != null) {
                node.value = value;
                removeNode(node);
                addNode(node);
            } else {
                if (map.size() >= capacity) {
                    Node<K, V> lru = tail.prev;
                    map.remove(lru.key);
                    removeNode(lru);
                    EventBus.getInstance().publish("CACHE_EVICTION", "Key_Evicted:" + lru.key);
                }
                Node<K, V> newNode = new Node<>(key, value);
                map.put(key, newNode);
                addNode(newNode);
            }
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    private void addNode(Node<K, V> node) {
        node.next = head.next;
        node.prev = head;
        head.next.prev = node;
        head.next = node;
    }

    private void removeNode(Node<K, V> node) {
        Node<K, V> prevNode = node.prev;
        Node<K, V> nextNode = node.next;
        prevNode.next = nextNode;
        nextNode.prev = prevNode;
    }
}
