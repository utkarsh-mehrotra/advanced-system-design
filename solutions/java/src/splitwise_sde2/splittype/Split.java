package splitwise_sde2.splittype;

import splitwise_sde2.User;
import java.math.BigDecimal;

public abstract class Split {
    protected final User user;
    protected BigDecimal amount; // Computed amount by strategy

    public Split(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    // Abstract identification of type to allow Strategy factory dispatch
    public abstract String getSplitType();
}
