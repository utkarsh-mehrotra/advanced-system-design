package librarymanagementsystem_sde2;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Member {
    private final String memberId;
    private final String name;
    private final String contactInfo;
    
    // SDE3: A Member's borrowed books list is heavily read (during checks) but minimally written to (max 5 limits).
    // The previous standard ArrayList caused ConcurrentModificationExceptions.
    // CopyOnWriteArrayList provides immaculate Thread-Safety for read-heavy iterative vectors!
    private final List<Book> borrowedBooks;

    public Member(String memberId, String name, String contactInfo) {
        this.memberId = memberId;
        this.name = name;
        this.contactInfo = contactInfo;
        this.borrowedBooks = new CopyOnWriteArrayList<>();
    }

    public void borrowBook(Book book) {
        borrowedBooks.add(book);
    }

    public void returnBook(Book book) {
        borrowedBooks.remove(book);
    }

    public String getMemberId() { return memberId; }
    public String getName() { return name; }
    public List<Book> getBorrowedBooks() { return borrowedBooks; }
}
