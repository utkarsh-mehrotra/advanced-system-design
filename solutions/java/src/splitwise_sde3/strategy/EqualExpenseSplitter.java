package splitwise_sde3.strategy;

import splitwise_sde3.exception.InvalidSplitException;
import splitwise_sde3.splittype.Split;
import splitwise_sde3.splittype.EqualSplit;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class EqualExpenseSplitter implements ExpenseSplitter {

    @Override
    public void calculateSplits(BigDecimal totalAmount, List<Split> splits) {
        if (splits.isEmpty()) return;

        // Verify all are EqualSplits
        for (Split split : splits) {
            if (!(split instanceof EqualSplit)) {
                throw new InvalidSplitException("All splits must be EqualSplits. Found: " + split.getClass().getSimpleName());
            }
        }

        int totalSplits = splits.size();
        BigDecimal splitCount = new BigDecimal(totalSplits);
        
        // SDE3: Precise division rounding rules.
        BigDecimal splitAmount = totalAmount.divide(splitCount, 2, RoundingMode.HALF_UP);

        // Distribute the baseline amount
        for (Split split : splits) {
            split.setAmount(splitAmount);
        }

        // Handle the remainder penny bleed (e.g., 100 / 3 = 33.33 * 3 = 99.99, remainder = 0.01)
        BigDecimal totalCalculated = splitAmount.multiply(splitCount);
        BigDecimal difference = totalAmount.subtract(totalCalculated);

        if (difference.compareTo(BigDecimal.ZERO) != 0) {
            // Give the remainder pennies to the first person. 
            // In a real system, you might allocate to the payer or randomly.
            Split firstSplit = splits.get(0);
            firstSplit.setAmount(firstSplit.getAmount().add(difference));
        }
    }
}
