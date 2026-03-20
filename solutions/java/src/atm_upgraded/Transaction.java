package atm_upgraded;

import java.math.BigDecimal;

public abstract class Transaction {
    protected final String transactionId;
    protected final Account account;
    protected final BigDecimal amount;

    public Transaction(String transactionId, Account account, BigDecimal amount) {
        this.transactionId = transactionId;
        this.account = account;
        this.amount = amount;
    }

    public abstract void execute();
}
