package digitalwallet_upgraded;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DigitalWalletDemoUpgraded {
    public static void main(String[] args) throws InterruptedException {
        WalletFacade wallet = new WalletFacade();

        // Setup Users
        User alice = new User("U1", "Alice", "alice@example.com");
        User bob = new User("U2", "Bob", "bob@example.com");
        wallet.registerUser(alice);
        wallet.registerUser(bob);

        // Setup Accounts
        // Alice has USD
        Account aliceAcc = new Account("ACC-ALICE", alice, "12345", Currency.USD);
        // Bob has EUR
        Account bobAcc = new Account("ACC-BOB", bob, "67890", Currency.EUR);

        // Seed initial money via backdoor deposit for demo
        aliceAcc.deposit(new BigDecimal("1000.00")); // $1000
        bobAcc.deposit(new BigDecimal("1000.00")); // 1000 EUR

        wallet.registerAccount(aliceAcc);
        wallet.registerAccount(bobAcc);

        System.out.println("--- Initial State ---");
        System.out.println("Alice Balance: " + aliceAcc.getBalance() + " " + aliceAcc.getCurrency());
        System.out.println("Bob Balance: " + bobAcc.getBalance() + " " + bobAcc.getCurrency());

        System.out.println("\n--- Executing SDE3 Multi-Threaded Deadlock-Free Transfers ---");
        
        // SDE3: Stress testing with concurrent bidirectional transfers
        int numberOfTransfers = 100;
        ExecutorService executor = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(numberOfTransfers * 2);

        // We will send $1 from Alice to Bob 100 times
        // And send 1 EUR from Bob to Alice 100 times concurrently!
        // If Lock ordering isn't perfect, this WILL deadlock and freeze.

        for (int i = 0; i < numberOfTransfers; i++) {
            executor.submit(() -> {
                try {
                    wallet.transfer(aliceAcc.getId(), bobAcc.getId(), new BigDecimal("1.00"), Currency.USD);
                } finally {
                    latch.countDown();
                }
            });

            executor.submit(() -> {
                try {
                    wallet.transfer(bobAcc.getId(), aliceAcc.getId(), new BigDecimal("1.00"), Currency.EUR);
                } finally {
                    latch.countDown();
                }
            });
        }

        // Wait for all 200 concurrent threads to safely finish!
        latch.await();
        executor.shutdown();

        System.out.println("All 200 concurrent cross-transfers completed safely without deadlocking!");

        System.out.println("\n--- Final State ---");
        System.out.println("Alice Balance: " + aliceAcc.getBalance() + " " + aliceAcc.getCurrency());
        System.out.println("Bob Balance: " + bobAcc.getBalance() + " " + bobAcc.getCurrency());
        
        // Math Check:
        // Alice sent 100 USD (deducted $100). Bob received 100 USD in EUR -> $100 * 0.85 = +85 EUR.
        // Bob sent 100 EUR (deducted 100 EUR). Alice received 100 EUR in USD -> 100 EUR / 0.85 = +117.65 USD.
        // Expected Alice: 1000 - 100 + 117.65 = 1017.65 USD
        // Expected Bob: 1000 + 85 - 100 = 985.00 EUR
    }
}
