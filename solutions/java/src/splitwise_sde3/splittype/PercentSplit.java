package splitwise_sde3.splittype;

import splitwise_sde3.User;

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
