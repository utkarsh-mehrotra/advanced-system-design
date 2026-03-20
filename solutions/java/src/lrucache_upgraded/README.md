# ⚡ LRU Cache — SDE3 Upgraded

## Overview
A fixed-capacity Least Recently Used cache with lock-striped concurrency. Instead of a global `synchronized` block, the cache is partitioned into independent segments — each with its own `ReentrantLock` — allowing parallel access across different key ranges.

## SDE3 Upgrades Applied

| Issue | Fix |
|-------|-----|
| `synchronized` on the whole cache — all threads serialize | Lock-striped segments via `LRUSegment[]`; threads on different keys never contend |
| Capacity eviction under global lock | Each `LRUSegment` independently manages its own doubly-linked list + capacity |
| No thread-safe iteration | `ReadWriteLock` per segment — readers share, writers are exclusive |

## Class Diagram

```mermaid
classDiagram
    class LRUCache~K,V~ {
        -LRUSegment~K,V~[] segments
        -int segmentCount
        +get(K key) V
        +put(K key, V value)
        -segmentFor(K key) LRUSegment
    }
    class LRUSegment~K,V~ {
        -int capacity
        -LinkedHashMap~K,Node~ map
        -ReentrantLock lock
        +get(K) V
        +put(K, V)
        -evict()
    }
    class Node~K,V~ {
        -K key
        -V value
        -Node prev
        -Node next
    }

    LRUCache "1" *-- "many" LRUSegment
    LRUSegment "1" *-- "many" Node
```

## Run
```bash
javac $(find lrucache_upgraded -name "*.java")
java lrucache_upgraded.LRUCacheDemoUpgraded
```
