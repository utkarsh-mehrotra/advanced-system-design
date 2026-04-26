package splitwise_sde3.splittype;

import splitwise_sde3.User;
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
