package splitwise_sde3;

public class SplitwiseDemoSDE3 {
    public static void main(String[] args) {
        new SettlementNotifier(); // Attach observers

        ExpenseManager manager = new ExpenseManager();
        AccountLedger u1 = new AccountLedger("U1");
        AccountLedger u2 = new AccountLedger("U2");
        
        manager.registerUser(u1);
        manager.registerUser(u2);

        System.out.println("Processing heavy split...");
        manager.addExpense("U1", "U2", 500.0);
        
        System.out.println("U2 Net Balance (Optimistic Read): " + u2.getBalance());
    }
}
