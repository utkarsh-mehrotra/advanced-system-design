package restaurantmanagementsystem_sde3;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Table {
    private final int tableId;
    private final int capacity;
    private TableStatus status;
    
    // SDE3: Granular concurrency block preventing double-seating.
    // If two hostesses attempt to seat 2 different parties sequentially at exactly the same topological table,
    // this explicit ReentrantLock ensures strict arbitration without freezing the whole restaurant facade.
    private final Lock lock;

    public Table(int tableId, int capacity) {
        this.tableId = tableId;
        this.capacity = capacity;
        this.status = TableStatus.AVAILABLE;
        this.lock = new ReentrantLock();
    }

    /**
     * Atomically secures the table for an incoming party.
     * @return true if successful, false if someone else just snatched it or if it is dirty.
     */
    public boolean lockAndOccupy() {
        lock.lock();
        try {
            if (status == TableStatus.AVAILABLE) {
                status = TableStatus.OCCUPIED;
                return true;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

    public void releaseTable() {
        lock.lock();
        try {
            status = TableStatus.AVAILABLE;
        } finally {
            lock.unlock();
        }
    }

    public int getTableId() { return tableId; }
    public int getCapacity() { return capacity; }
    
    // Readonly lock
    public TableStatus getStatus() {
        lock.lock();
        try {
            return status;
        } finally {
            lock.unlock();
        }
    }
}
