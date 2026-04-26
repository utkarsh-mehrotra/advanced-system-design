package splitwise_sde3.strategy;

import java.math.BigDecimal;
import java.util.List;
import splitwise_sde3.splittype.Split;

public interface ExpenseSplitter {
    /**
     * Calculates and sets the split amounts for a particular strategy.
     * @param totalAmount Exact total amount of the expense
     * @param splits The list of splits provided by the user
     * @throws splitwise_sde3.exception.InvalidSplitException if validation fails
     */
    void calculateSplits(BigDecimal totalAmount, List<Split> splits);
}
