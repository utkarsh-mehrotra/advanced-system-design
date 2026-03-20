package splitwise_upgraded.strategy;

import splitwise_upgraded.exception.InvalidSplitException;
import splitwise_upgraded.splittype.PercentSplit;
import splitwise_upgraded.splittype.Split;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class PercentExpenseSplitter implements ExpenseSplitter {

    @Override
    public void calculateSplits(BigDecimal totalAmount, List<Split> splits) {
        double totalPercent = 0;

        for (Split split : splits) {
            if (!(split instanceof PercentSplit)) {
                throw new InvalidSplitException("All splits must be PercentSplits.");
            }
            totalPercent += ((PercentSplit) split).getPercent();
        }

        if (Math.abs(totalPercent - 100.0) > 0.001) {
            throw new InvalidSplitException("Total percentage must equal 100%. Got: " + totalPercent);
        }

        BigDecimal calculatedTotal = BigDecimal.ZERO;

        for (Split split : splits) {
            PercentSplit pSplit = (PercentSplit) split;
            BigDecimal percent = BigDecimal.valueOf(pSplit.getPercent());
            
            // Amount = total * (percent / 100)
            BigDecimal splitAmount = totalAmount.multiply(percent)
                    .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
            
            split.setAmount(splitAmount);
            calculatedTotal = calculatedTotal.add(splitAmount);
        }

        // Remainder penny fix 
        BigDecimal difference = totalAmount.subtract(calculatedTotal);
        if (difference.compareTo(BigDecimal.ZERO) != 0) {
            Split firstSplit = splits.get(0);
            firstSplit.setAmount(firstSplit.getAmount().add(difference));
        }
    }
}
