package atm_upgraded;

import java.math.BigDecimal;

public class DepositTransaction extends Transaction {
    public DepositTransaction(String transactionId, Account account, BigDecimal amount) {
        super(transactionId, account, amount);
    }

    @Override
    public void execute() {
        account.credit(amount);
    }
}
