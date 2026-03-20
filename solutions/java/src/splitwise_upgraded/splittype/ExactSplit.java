package splitwise_upgraded.splittype;

import splitwise_upgraded.User;
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
