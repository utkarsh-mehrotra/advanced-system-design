package librarymanagementsystem_sde2;

import java.util.concurrent.atomic.AtomicBoolean;

public class Book {
    private final String isbn;
    private final String title;
    private final String author;
    private final int publicationYear;
    
    // SDE3: Granular intrinsic safety!
    // Replacing a standard boolean and external synchronization with
    // a lightweight, ultra-performant Atomic Compare-And-Swap (CAS) mechanic.
    private final AtomicBoolean available;

    public Book(String isbn, String title, String author, int publicationYear) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publicationYear = publicationYear;
        // All new books begin as freely available
        this.available = new AtomicBoolean(true);
    }

    public String getIsbn() { return isbn; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public int getPublicationYear() { return publicationYear; }

    public boolean isAvailable() {
        return available.get();
    }

    /**
     * SDE3: Solves the fatal TOCTOU (Time-Of-Check to Time-Of-Use) bug.
     * Atomically flips the status from true to false exactly ONCE.
     * @return true if this exact thread won the race and secured the book.
     */
    public boolean reserve() {
        return available.compareAndSet(true, false);
    }

    /**
     * Atomically returns the book back to circulation.
     */
    public void release() {
        available.set(true);
    }
}
