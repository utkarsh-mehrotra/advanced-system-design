package atm_upgraded;

import java.math.BigDecimal;

public class WithdrawalTransaction extends Transaction {
    public WithdrawalTransaction(String transactionId, Account account, BigDecimal amount) {
        super(transactionId, account, amount);
    }

    @Override
    public void execute() {
        account.debit(amount);
    }
}
