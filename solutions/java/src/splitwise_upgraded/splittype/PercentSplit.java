package splitwise_upgraded.splittype;

import splitwise_upgraded.User;

public class PercentSplit extends Split {
    public static final String TYPE = "PERCENT";
    private final double percent; // Kept as double for percentage defining (e.g. 33.33)

    public PercentSplit(User user, double percent) {
        super(user);
        this.percent = percent;
    }

    public double getPercent() {
        return percent;
    }

    @Override
    public String getSplitType() {
        return TYPE;
    }
}
