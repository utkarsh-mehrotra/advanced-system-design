package librarymanagementsystem_sde3;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Catalog {
    private final Map<String, Book> inventory = new ConcurrentHashMap<>();
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();

    public void addBook(Book book) {
        // Assume single librarian writer or infrequent writes
        rwLock.writeLock().lock();
        try {
            inventory.put(book.getIsbn(), book);
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    public void displayCatalog() {
        // Ultra-high concurrency reads allowing thousands of digital users to browse simultaneously
        rwLock.readLock().lock();
        try {
            inventory.values().forEach(b -> 
                System.out.println("Book: " + b.getTitle() + " | Available: " + b.checkAvailability())
            );
        } finally {
            rwLock.readLock().unlock();
        }
    }

    public Book fetchReference(String isbn) {
        // Non-blocking fetch
        return inventory.get(isbn);
    }
}
