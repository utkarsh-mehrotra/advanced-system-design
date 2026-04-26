package librarymanagementsystem_sde3;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LibraryManagementSystemDemoUpgraded {
    public static void main(String[] args) throws InterruptedException {
        Library nyLibrary = new Library("NY Public Library Central");

        nyLibrary.registerMember(new Member("M1", "Alice", "alice@corp.com"));
        nyLibrary.registerMember(new Member("M2", "Bob", "bob@corp.com"));

        Book javaBook = new Book("TEST-001", "Effective Java", "Joshua Bloch", 2018);
        Book cleanCode = new Book("TEST-002", "Clean Code", "Robert C. Martin", 2008);
        
        nyLibrary.addBook(javaBook);
        nyLibrary.addBook(cleanCode);

        System.out.println("--- Library SDE3 Lock-Free Concurrency Tests ---");
        
        // Simulating the race condition (TOCTOU failure in the original system)
        ExecutorService executor = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(10);

        System.out.println("10 threads attempting to checkout 'Effective Java' simultaneously...");
        
        for (int i = 0; i < 10; i++) {
            executor.submit(() -> {
                try {
                    // Try to borrow exactly the same book!
                    nyLibrary.borrowBook("M1", "TEST-001");
                } finally {
                    latch.countDown();
                }
            });
        }
        
        latch.await();
        executor.shutdown();
        
        System.out.println("\n--- Java 8 Parallel Stream Filtering ---");
        long start = System.currentTimeMillis();
        List<Book> searchResults = nyLibrary.searchBooks("clean");
        long end = System.currentTimeMillis();
        
        System.out.println("Found " + searchResults.size() + " books matching 'clean' in " + (end - start) + "ms utilizing ForkJoin Parallel CPU pools.");
        System.out.println("Result Check: " + searchResults.get(0).getTitle());
    }
}
