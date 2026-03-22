package digitalwallet_sde2;

import digitalwallet_sde2.exception.InsufficientFundsException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Account {
    private final String id;
    private final User user;
    private final String accountNumber;
    private final Currency currency;
    
    // Protected by explicit locks rather than implicit monitors
    private BigDecimal balance;
    private final List<Transaction> transactions;
    
    // SDE3: Exposing a Lock to the TransactionService so it can apply Lock Ordering
    // to absolutely prevent Account deadlocks during multi-directional transfers.
    private final Lock accountLock;

    public Account(String id, User user, String accountNumber, Currency currency) {
        this.id = id;
        this.user = user;
        this.accountNumber = accountNumber;
        this.currency = currency;
        this.balance = BigDecimal.ZERO;
        this.transactions = new ArrayList<>();
        this.accountLock = new ReentrantLock();
    }

    public void deposit(BigDecimal amount) {
        accountLock.lock();
        try {
            balance = balance.add(amount);
        } finally {
            accountLock.unlock();
        }
    }

    public void withdraw(BigDecimal amount) {
        accountLock.lock();
        try {
            if (balance.compareTo(amount) >= 0) {
                balance = balance.subtract(amount);
            } else {
                throw new InsufficientFundsException("Insufficient funds in account " + id);
            }
        } finally {
            accountLock.unlock();
        }
    }

    public void addTransaction(Transaction transaction) {
        accountLock.lock();
        try {
            transactions.add(transaction);
        } finally {
            accountLock.unlock();
        }
    }

    public String getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public Currency getCurrency() {
        return currency;
    }

    public BigDecimal getBalance() {
        accountLock.lock();
        try {
            return balance;
        } finally {
            accountLock.unlock();
        }
    }

    public List<Transaction> getTransactions() {
        accountLock.lock();
        try {
            return Collections.unmodifiableList(new ArrayList<>(transactions));
        } finally {
            accountLock.unlock();
        }
    }

    public Lock getLock() {
        return accountLock;
    }
}
