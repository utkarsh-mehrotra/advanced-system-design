package librarymanagementsystem_sde3;

public class LibraryManager {
    private final Catalog catalog;

    public LibraryManager(Catalog catalog) {
        this.catalog = catalog;
    }

    public void borrowTarget(String userId, String isbn) {
        Book b = catalog.fetchReference(isbn);
        if (b != null && b.borrowBook()) {
            System.out.println("LibraryManager: " + isbn + " successfully granted to " + userId);
            
            // Background due-date configuration and email firing decoupled
            EventBus.getInstance().publish("BOOK_BORROWED", userId + ":" + isbn);
        } else {
            System.out.println("LibraryManager: " + isbn + " is currently unavailable.");
        }
    }
}
