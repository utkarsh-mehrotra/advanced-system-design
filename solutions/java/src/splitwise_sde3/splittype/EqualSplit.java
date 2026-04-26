package splitwise_sde3.splittype;

import splitwise_sde3.User;

public class EqualSplit extends Split {
    public static final String TYPE = "EQUAL";

    public EqualSplit(User user) {
        super(user);
    }

    @Override
    public String getSplitType() {
        return TYPE;
    }
}
