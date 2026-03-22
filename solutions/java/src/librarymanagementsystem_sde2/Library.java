package librarymanagementsystem_sde2;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Replaces the LibraryManager Singleton.
 * Devoid of global sync locks!
 */
public class Library {
    private final String branchName;
    private final Map<String, Book> catalog;
    private final Map<String, Member> members;
    
    private static final int MAX_BOOKS_PER_MEMBER = 5;

    public Library(String branchName) {
        this.branchName = branchName;
        // Concurrent HashMaps to permit parallel O(1) reads/writes globally
        this.catalog = new ConcurrentHashMap<>();
        this.members = new ConcurrentHashMap<>();
    }

    public void addBook(Book book) {
        catalog.put(book.getIsbn(), book);
    }

    public void registerMember(Member member) {
        members.put(member.getMemberId(), member);
    }

    // SDE3: NO global "synchronized" bottleneck.
    public void borrowBook(String memberId, String isbn) {
        Member member = members.get(memberId);
        Book book = catalog.get(isbn);

        if (member == null || book == null) {
            System.out.println("Invalid member or book reference.");
            return;
        }

        if (member.getBorrowedBooks().size() >= MAX_BOOKS_PER_MEMBER) {
            System.out.println("Member " + member.getName() + " reached their 5-book maximum.");
            return;
        }

        // SDE3: The core Atomic Operation.
        // Even if 100 threads try to borrow this exact specific book, ONLY ONE will pass the threshold.
        // It locks ONLY the book, not the Library!
        if (book.reserve()) {
            member.borrowBook(book);
            System.out.println("Success! [" + branchName + "] " + member.getName() + " checked out " + book.getTitle());
        } else {
            System.out.println("Failed: " + book.getTitle() + " is already currently borrowed by someone else.");
        }
    }

    // SDE3: Lock-free returns!
    public void returnBook(String memberId, String isbn) {
        Member member = members.get(memberId);
        Book book = catalog.get(isbn);

        if (member != null && book != null) {
            member.returnBook(book);
            book.release(); // Free the atomic boolean
            System.out.println("Returned: " + book.getTitle() + " smoothly back to " + branchName);
        }
    }

    /**
     * SDE3: Massive O(N) linear reduction via Parallel Streams.
     * Automatically shards the catalog Array iteration across multiple CPU Cores asynchronously.
     */
    public List<Book> searchBooks(String keyword) {
        final String searchStr = keyword.toLowerCase();
        
        return catalog.values().parallelStream()
                .filter(book -> book.getTitle().toLowerCase().contains(searchStr) || 
                                book.getAuthor().toLowerCase().contains(searchStr))
                .collect(Collectors.toList());
    }
}
