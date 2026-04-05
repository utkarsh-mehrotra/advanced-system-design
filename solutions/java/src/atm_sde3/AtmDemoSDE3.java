package atm_sde3;

public class AtmDemoSDE3 {
    public static void main(String[] args) {
        new CashDispenser(); // Async component

        Account premiumAccount = new Account("123456789", 5000.0);
        AtmController controller = new AtmController();

        System.out.println("User requesting $500...");
        controller.processWithdrawal(premiumAccount, 500.0);

        System.out.println("Remaining Balance (Optimistic Read): $" + premiumAccount.getBalance());
    }
}
