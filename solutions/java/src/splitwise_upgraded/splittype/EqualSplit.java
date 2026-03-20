package splitwise_upgraded.splittype;

import splitwise_upgraded.User;

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
