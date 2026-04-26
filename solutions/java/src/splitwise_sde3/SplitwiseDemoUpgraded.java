package splitwise_sde3;

import splitwise_sde3.splittype.*;

import java.math.BigDecimal;
import java.util.Arrays;

public class SplitwiseDemoUpgraded {
    public static void main(String[] args) {
        BalanceManager balanceManager = new BalanceManager();
        SplitwiseService service = new SplitwiseService(balanceManager);

        // Users
        User u1 = new User("U1", "Alice", "alice@example.com");
        User u2 = new User("U2", "Bob", "bob@example.com");
        User u3 = new User("U3", "Charlie", "charlie@example.com");

        service.registerUser(u1);
        service.registerUser(u2);
        service.registerUser(u3);

        // Group
        Group g1 = new Group("G1", "Roommates");
        g1.addMember(u1);
        g1.addMember(u2);
        g1.addMember(u3);
        service.registerGroup(g1);

        System.out.println("--- Demo 1: Equal Splits fixing penny bleed ---");
        // Alice pays $100 for internet, split equally among 3 users
        // The EqualSplitStrategy should assign $33.34 to Alice, $33.33 to Bob, $33.33 to Charlie 
        Expense exp1 = new Expense("EXP-1", new BigDecimal("100"), "Internet", u1, Arrays.asList(
                new EqualSplit(u1),
                new EqualSplit(u2),
                new EqualSplit(u3)
        ));
        service.addExpense(g1.getId(), exp1);
        
        balanceManager.printBalances(); // Bob owes Alice 33.33, Charlie owes Alice 33.33

        System.out.println("\n--- Demo 2: Exact Splits ---");
        // Bob pays $20 for Pizza. Charlie owes 15, Alice owes 5.
        Expense exp2 = new Expense("EXP-2", new BigDecimal("20"), "Pizza", u2, Arrays.asList(
                new ExactSplit(u3, new BigDecimal("15")),
                new ExactSplit(u1, new BigDecimal("5"))
        ));
        service.addExpense(g1.getId(), exp2);

        balanceManager.printBalances();

        System.out.println("\n--- Demo 3: Percentage Splits ---");
        // Charlie pays $50 for Uber. Charlie pays 50%, Alice 25%, Bob 25%
        Expense exp3 = new Expense("EXP-3", new BigDecimal("50"), "Uber", u3, Arrays.asList(
                new PercentSplit(u3, 50.0),
                new PercentSplit(u1, 25.0),
                new PercentSplit(u2, 25.0)
        ));
        service.addExpense(g1.getId(), exp3);

        balanceManager.printBalances();

        System.out.println("\n--- Demo 4: Settling Up ---");
        // Bob settles up with Alice
        service.settleUp(u2.getId(), u1.getId());
        balanceManager.printBalances();
    }
}
