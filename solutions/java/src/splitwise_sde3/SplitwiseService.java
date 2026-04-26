package splitwise_sde3;

import splitwise_sde3.splittype.*;
import splitwise_sde3.strategy.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Splitwise orchestration service.
 * Eliminates singletons and God-Class behaviors by delegating to Strategies and BalanceManager.
 */
public class SplitwiseService {
    
    private final Map<String, User> userRegistry;
    private final Map<String, Group> groupRegistry;
    private final BalanceManager balanceManager;

    // Strategy Registries avoiding massive if-else chains
    private final Map<String, ExpenseSplitter> splitterFactory;

    public SplitwiseService(BalanceManager balanceManager) {
        this.userRegistry = new ConcurrentHashMap<>();
        this.groupRegistry = new ConcurrentHashMap<>();
        this.balanceManager = balanceManager;
        
        this.splitterFactory = new ConcurrentHashMap<>();
        // Registering OCP compliant strategies
        this.splitterFactory.put(EqualSplit.TYPE, new EqualExpenseSplitter());
        this.splitterFactory.put(ExactSplit.TYPE, new ExactExpenseSplitter());
        this.splitterFactory.put(PercentSplit.TYPE, new PercentExpenseSplitter());
    }

    public void registerUser(User user) {
        userRegistry.put(user.getId(), user);
    }

    public void registerGroup(Group group) {
        groupRegistry.put(group.getId(), group);
    }

    public void addExpense(String groupId, Expense expense) {
        Group group = groupRegistry.get(groupId);
        if (group != null) {
            group.addExpense(expense);
        }

        // Validate splits aren't empty
        if (expense.getSplits().isEmpty()) return;

        // Factory fetches strategy dynamically
        String splitType = expense.getSplits().get(0).getSplitType();
        ExpenseSplitter splitter = splitterFactory.get(splitType);

        if (splitter == null) {
            throw new IllegalArgumentException("Unsupported Split Type: " + splitType);
        }

        // 1. Calculates constraints and computes fractional values
        splitter.calculateSplits(expense.getAmount(), expense.getSplits());

        // 2. Dispatch atomic ledger updates
        updateBalances(expense);
    }

    private void updateBalances(Expense expense) {
        User paidBy = expense.getPaidBy();

        for (Split split : expense.getSplits()) {
            User borrowedBy = split.getUser();
            
            // If the person who paid is the same person who owes on the split, don't update ledger
            if (!paidBy.equals(borrowedBy)) {
                // borrowedBy owes paidBy
                balanceManager.addOwedAmount(borrowedBy.getId(), paidBy.getId(), split.getAmount());
            }
        }
    }

    public void settleUp(String userIdOwes, String userIdOwed) {
        balanceManager.settleUp(userIdOwes, userIdOwed);
        System.out.println("Settled up balances between " + userIdOwes + " and " + userIdOwed);
    }

    public BalanceManager getBalanceManager() {
        return balanceManager;
    }
}
