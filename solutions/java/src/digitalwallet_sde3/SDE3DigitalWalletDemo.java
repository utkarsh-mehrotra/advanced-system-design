package digitalwallet_sde3;

import digitalwallet_sde3.database.LedgerDatabase;
import digitalwallet_sde3.database.SagaStore;
import digitalwallet_sde3.service.AccountLedgerService;
import digitalwallet_sde3.service.TransferSagaCoordinator;
import digitalwallet_sde3.service.WalletGatewayService;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SDE3DigitalWalletDemo {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("🚀 Starting SDE3 Event-Sourced Digital Wallet (Distributed Saga Concept)...");

        // 1. Initialize infra
        LedgerDatabase ledgerDb = new LedgerDatabase();
        SagaStore sagaStore = new SagaStore();

        // 2. Initialize Microservices
        AccountLedgerService ledgerService = new AccountLedgerService(ledgerDb);
        TransferSagaCoordinator sagaCoordinator = new TransferSagaCoordinator(sagaStore, ledgerService);
        WalletGatewayService gateway = new WalletGatewayService(sagaStore);

        // 3. Setup Accounts
        ledgerService.createAccount("ALICE", new BigDecimal("1000.00"));
        ledgerService.createAccount("BOB", new BigDecimal("1000.00"));
        
        System.out.println("\n--- Starting High-Concurrency Deadlock-Free Transfers ---");

        // We will perform 100 simultaneous transfers A -> B ($1) and B -> A ($2)
        // In the legacy system, this caused Deadlocks unless lexicographically sorted `ReentrantLock`s were used.
        // In SDE3 Saga, there are no multi-account locks, so deadlock is mathematically impossible.
        int numTransfers = 500;
        CountDownLatch latch = new CountDownLatch(numTransfers);
        ExecutorService multiThreadedClients = Executors.newFixedThreadPool(10);

        for (int i = 0; i < numTransfers; i++) {
            final int index = i;
            multiThreadedClients.submit(() -> {
                if (index % 2 == 0) {
                    // Alice sends $1 to Bob
                    gateway.initiateTransfer("ALICE", "BOB", new BigDecimal("1.00"));
                } else {
                    // Bob sends $2 to Alice
                    gateway.initiateTransfer("BOB", "ALICE", new BigDecimal("2.00"));
                }
                latch.countDown();
            });
        }

        latch.await();
        multiThreadedClients.shutdown();

        System.out.println("\nAll HTTP requests accepted in O(1). Waiting for Saga async pipelines to settle...");
        Thread.sleep(3000); // Wait for Sagas to finish

        System.out.println("\n--- Final Distributed Ledger Balances ---");
        // Alice: started with 1000. Sent 250*1 = 250. Received 250*2 = 500. Expected: 1250.00
        System.out.println("Alice Balance: $" + ledgerService.getAccountState("ALICE").getAvailableBalance());
        
        // Bob: started with 1000. Sent 250*2 = 500. Received 250*1 = 250. Expected: 750.00
        System.out.println("Bob Balance: $" + ledgerService.getAccountState("BOB").getAvailableBalance());

        // Shutdown async threads
        ledgerService.shutdown();
        sagaCoordinator.shutdown();
        System.exit(0);
    }
}
