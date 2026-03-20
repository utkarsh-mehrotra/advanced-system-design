package digitalwallet_upgraded;

import digitalwallet_upgraded.service.TransactionService;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Facade replacing the DigitalWallet Singleton God-Class.
 * Handles entity registries and delegates core logic.
 */
public class WalletFacade {
    private final Map<String, User> userRegistry;
    private final Map<String, Account> accountRegistry;
    private final Map<String, PaymentMethod> paymentMethods;
    
    private final TransactionService transactionService;

    public WalletFacade() {
        this.userRegistry = new ConcurrentHashMap<>();
        this.accountRegistry = new ConcurrentHashMap<>();
        this.paymentMethods = new ConcurrentHashMap<>();
        this.transactionService = new TransactionService();
    }

    public void registerUser(User user) {
        userRegistry.put(user.getId(), user);
    }

    public void registerAccount(Account account) {
        accountRegistry.put(account.getId(), account);
    }

    public void registerPaymentMethod(PaymentMethod paymentMethod) {
        paymentMethods.put(paymentMethod.getId(), paymentMethod);
    }
    
    public Account getAccount(String accountId) {
        return accountRegistry.get(accountId);
    }

    /**
     * Executes a transfer. Note the lack of 'synchronized' here, allowing 
     * millions of parallel independent transfers!
     */
    public Transaction transfer(String sourceAccountId, String destAccountId, BigDecimal amount, Currency currency) {
        Account source = accountRegistry.get(sourceAccountId);
        Account dest = accountRegistry.get(destAccountId);

        if (source == null || dest == null) {
            throw new IllegalArgumentException("Invalid source or destination account.");
        }

        return transactionService.transferFunds(source, dest, amount, currency);
    }
}
