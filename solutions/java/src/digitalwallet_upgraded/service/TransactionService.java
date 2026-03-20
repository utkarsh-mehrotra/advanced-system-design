package digitalwallet_upgraded.service;

import digitalwallet_upgraded.Account;
import digitalwallet_upgraded.Currency;
import digitalwallet_upgraded.Transaction;
import digitalwallet_upgraded.TransactionStatus;

import java.math.BigDecimal;
import java.util.UUID;

public class TransactionService {

    /**
     * Executes a funds transfer using strict Lexicographical Lock Ordering.
     * This absolutely prevents deadlocks when A transfers to B, and B transfers to A concurrently.
     */
    public Transaction transferFunds(Account source, Account destination, BigDecimal rawAmount, Currency transferCurrency) {
        
        // SDE3: Determine Lock Ordering by ID to prevent Deadlocks
        Account firstLock;
        Account secondLock;
        
        if (source.getId().compareTo(destination.getId()) < 0) {
            firstLock = source;
            secondLock = destination;
        } else {
            firstLock = destination;
            secondLock = source;
        }

        Transaction transactionReceipt;

        // 1. Acquire Locks in strict order
        firstLock.getLock().lock();
        try {
            secondLock.getLock().lock();
            try {
                // 2. Critical Section (Both accounts safely locked)
                
                // Convert exactly what needs to be withdrawn
                BigDecimal withdrawAmount = CurrencyConverter.convert(rawAmount, transferCurrency, source.getCurrency());
                source.withdraw(withdrawAmount);

                // Convert exactly what needs to be deposited
                BigDecimal depositAmount = CurrencyConverter.convert(rawAmount, transferCurrency, destination.getCurrency());
                destination.deposit(depositAmount);

                String txnId = generateTransactionId();
                transactionReceipt = new Transaction(txnId, source, destination, rawAmount, transferCurrency, TransactionStatus.COMPLETED);
                
                // Add receipt to ledgers
                source.addTransaction(transactionReceipt);
                destination.addTransaction(transactionReceipt);
                
            } finally {
                secondLock.getLock().unlock();
            }
        } finally {
            firstLock.getLock().unlock();
        }

        return transactionReceipt;
    }

    private String generateTransactionId() {
        return "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
