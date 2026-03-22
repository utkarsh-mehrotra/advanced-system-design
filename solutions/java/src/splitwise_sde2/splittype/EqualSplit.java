package splitwise_sde2.splittype;

import splitwise_sde2.User;

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
