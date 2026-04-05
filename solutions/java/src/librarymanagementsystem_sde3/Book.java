package librarymanagementsystem_sde3;

import java.util.concurrent.atomic.AtomicBoolean;

public class Book {
    private final String isbn;
    private final String title;
    // Lock-free primitive for exact copies
    private final AtomicBoolean isAvailable;

    public Book(String isbn, String title) {
        this.isbn = isbn;
        this.title = title;
        this.isAvailable = new AtomicBoolean(true);
    }

    public String getIsbn() { return isbn; }
    public String getTitle() { return title; }
    public boolean checkAvailability() { return isAvailable.get(); }

    public boolean borrowBook() {
        return isAvailable.compareAndSet(true, false);
    }

    public boolean returnBook() {
        return isAvailable.compareAndSet(false, true);
    }
}
