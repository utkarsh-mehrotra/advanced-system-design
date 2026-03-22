package splitwise_sde2.splittype;

import splitwise_sde2.User;
import java.math.BigDecimal;

public class ExactSplit extends Split {
    public static final String TYPE = "EXACT";

    public ExactSplit(User user, BigDecimal amount) {
        super(user);
        this.amount = amount;
    }

    @Override
    public String getSplitType() {
        return TYPE;
    }
}
