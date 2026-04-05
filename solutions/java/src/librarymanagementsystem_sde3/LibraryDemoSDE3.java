package librarymanagementsystem_sde3;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class LibraryDemoSDE3 {
    public static void main(String[] args) throws InterruptedException {
        new AuditingService();

        Catalog catalog = new Catalog();
        catalog.addBook(new Book("ISBN_001", "Design Patterns Elements"));
        
        LibraryManager manager = new LibraryManager(catalog);

        // Simulation of multiple readers browsing globally while one borrows
        ExecutorService executor = Executors.newFixedThreadPool(5);
        for(int i = 0; i < 4; i++) {
            executor.submit(() -> {
                catalog.displayCatalog(); // Heavy lock-free read
            });
        }
        
        executor.submit(() -> {
            manager.borrowTarget("USER_10", "ISBN_001");
        });

        executor.shutdown();
        executor.awaitTermination(2, TimeUnit.SECONDS);
    }
}
