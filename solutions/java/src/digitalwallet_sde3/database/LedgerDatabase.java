package digitalwallet_sde3.database;

import digitalwallet_sde3.model.Account;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simulates a durable Ledger Database (CockroachDB/PostgreSQL).
 */
public class LedgerDatabase {
    private final ConcurrentHashMap<String, Account> accounts = new ConcurrentHashMap<>();

    public void save(Account account) {
        accounts.put(account.getAccountId(), account);
    }

    public Account getAccount(String accountId) {
        return accounts.get(accountId);
    }
}
