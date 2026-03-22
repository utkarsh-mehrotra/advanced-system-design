package onlinestockbrokeragesystem_sde3;

import onlinestockbrokeragesystem_sde3.model.OrderSide;
import onlinestockbrokeragesystem_sde3.service.LedgerService;
import onlinestockbrokeragesystem_sde3.service.MatchingEngineService;
import onlinestockbrokeragesystem_sde3.service.OrderGatewayService;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SDE3StockBrokerageDemo {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("🚀 Starting SDE3 Distributed Stock Brokerage (Event-Driven & Lock-Free)...");

        // 1. Initialize Distributed Infrastructure
        LedgerService ledgerService = new LedgerService();
        OrderGatewayService gateway = new OrderGatewayService(ledgerService);
        
        // Boot up matching engines for heavily traded partitions
        MatchingEngineService aaplEngine = new MatchingEngineService("AAPL");
        MatchingEngineService tslaEngine = new MatchingEngineService("TSLA");

        // 2. Fund Accounts
        ledgerService.createAccount("ALICE", new BigDecimal("100000.00"));
        ledgerService.createAccount("BOB", new BigDecimal("50000.00"));
        ledgerService.createAccount("CHARLIE", new BigDecimal("0.00"));

        // Pre-fund Bob with some AAPL so he can sell it
        ledgerService.getPortfolio("BOB").addPosition("AAPL", 100);

        System.out.println("\n--- Starting High-Frequency Trading Demo ---");

        int numberOfOrders = 500;
        CountDownLatch latch = new CountDownLatch(numberOfOrders);
        ExecutorService pumper = Executors.newFixedThreadPool(10);

        // Stress Test: Bombard the Gateway with concurrent concurrent orders
        for (int i = 0; i < numberOfOrders; i++) {
            final int index = i;
            pumper.submit(() -> {
                if (index % 2 == 0) {
                    gateway.placeOrder("ALICE", "AAPL", OrderSide.BUY, 2, new BigDecimal("150.00"));
                } else {
                    gateway.placeOrder("BOB", "AAPL", OrderSide.SELL, 2, new BigDecimal("149.00"));
                }
                latch.countDown();
            });
        }

        // Wait for all HTTP requests to be submitted
        latch.await();
        pumper.shutdown();

        // 4. Wait for async pipelines to flush (Gateway -> Kafka -> MatchEngine -> Kafka -> Ledger)
        System.out.println("Waiting for asynchronous settlement pipeline...");
        Thread.sleep(3000); 

        // 5. Final Balances
        System.out.println("\n--- Final Ledger State ---");
        System.out.println("Alice Balance: $" + ledgerService.getAccount("ALICE").getAvailableBalance());
        System.out.println("Alice AAPL Holdings: " + ledgerService.getPortfolio("ALICE").getAvailableHoldings("AAPL"));
        System.out.println("Bob Balance: $" + ledgerService.getAccount("BOB").getAvailableBalance());
        System.out.println("Bob AAPL Holdings: " + ledgerService.getPortfolio("BOB").getAvailableHoldings("AAPL"));

        // Shutdown threads
        aaplEngine.shutdown();
        tslaEngine.shutdown();
        ledgerService.shutdown();
        System.exit(0);
    }
}
