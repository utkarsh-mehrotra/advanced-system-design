package atm_sde2;

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
