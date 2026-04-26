package splitwise_sde3.strategy;

import splitwise_sde3.exception.InvalidSplitException;
import splitwise_sde3.splittype.ExactSplit;
import splitwise_sde3.splittype.Split;

import java.math.BigDecimal;
import java.util.List;

public class ExactExpenseSplitter implements ExpenseSplitter {

    @Override
    public void calculateSplits(BigDecimal totalAmount, List<Split> splits) {
        BigDecimal sum = BigDecimal.ZERO;

        for (Split split : splits) {
            if (!(split instanceof ExactSplit)) {
                throw new InvalidSplitException("All splits must be ExactSplits.");
            }
            sum = sum.add(split.getAmount());
        }

        // Validate that exact splits perfectly match the total expense
        if (sum.compareTo(totalAmount) != 0) {
            throw new InvalidSplitException(
                String.format("Exact splits sum (%s) does not match total expense amount (%s)", sum, totalAmount)
            );
        }
    }
}
