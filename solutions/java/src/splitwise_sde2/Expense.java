package splitwise_sde2;

import splitwise_sde2.splittype.Split;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public class Expense {
    private final String id;
    private final BigDecimal amount; // SDE2+: Mandatory usage of BigDecimal for precise financial math
    private final String description;
    private final User paidBy;
    private final List<Split> splits;

    public Expense(String id, BigDecimal amount, String description, User paidBy, List<Split> splits) {
        this.id = id;
        this.amount = amount;
        this.description = description;
        this.paidBy = paidBy;
        
        // Defensive copy ensuring structural immutability of an Expense once created
        this.splits = List.copyOf(splits);
    }

    public String getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public User getPaidBy() {
        return paidBy;
    }

    public List<Split> getSplits() {
        return splits;
    }
}
