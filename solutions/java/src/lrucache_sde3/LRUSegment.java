package lrucache_sde3;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * An isolated shard of the LRU Cache.
 * Contains purely its own Hash Array, Doubly Linked List, and an exclusive ReentrantLock.
 * Never contends with other Segments!
 */
class LRUSegment<K, V> {
    private final int capacity;
    private final Map<K, Node<K, V>> map;
    private final Node<K, V> head;
    private final Node<K, V> tail;

    // SDE3: Granular Segment Lock instead of the horrific 'public synchronized' on the parent class.
    private final Lock lock;

    public LRUSegment(int capacity) {
        this.capacity = capacity;
        this.map = new HashMap<>(capacity);
        this.lock = new ReentrantLock();
        
        this.head = new Node<>(null, null);
        this.tail = new Node<>(null, null);
        head.next = tail;
        tail.prev = head;
    }

    public V get(K key) {
        lock.lock();
        try {
            Node<K, V> node = map.get(key);
            if (node == null) return null;
            
            moveToHead(node);
            return node.value;
        } finally {
            lock.unlock();
        }
    }

    public void put(K key, V value) {
        lock.lock();
        try {
            Node<K, V> node = map.get(key);
            if (node != null) {
                node.value = value;
                moveToHead(node);
            } else {
                node = new Node<>(key, value);
                map.put(key, node);
                addToHead(node);
                
                if (map.size() > capacity) {
                    Node<K, V> removedNode = removeTail();
                    map.remove(removedNode.key);
                }
            }
        } finally {
            lock.unlock();
        }
    }

    private void addToHead(Node<K, V> node) {
        node.prev = head;
        node.next = head.next;
        head.next.prev = node;
        head.next = node;
    }

    private void removeNode(Node<K, V> node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    private void moveToHead(Node<K, V> node) {
        removeNode(node);
        addToHead(node);
    }

    private Node<K, V> removeTail() {
        Node<K, V> node = tail.prev;
        removeNode(node);
        return node;
    }
}
