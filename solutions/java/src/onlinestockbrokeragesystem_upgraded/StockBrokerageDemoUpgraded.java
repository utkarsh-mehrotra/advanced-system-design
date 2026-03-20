package onlinestockbrokeragesystem_upgraded;

import java.math.BigDecimal;

public class StockBrokerageDemoUpgraded {
    public static void main(String[] args) {
        StockBrokerageFacade broker = new StockBrokerageFacade();

        // Two trading participants
        User alice = new User("U1", "Alice");
        User bob   = new User("U2", "Bob");

        Account aliceAcc = broker.createAccount(alice, new BigDecimal("10000.00"));
        Account bobAcc   = broker.createAccount(bob,   new BigDecimal("10000.00"));

        // Give Bob some AAPL shares to sell
        broker.getPortfolio(bobAcc).addPosition("AAPL", 50);
        System.out.println("Bob's initial AAPL holdings: " + broker.getPortfolio(bobAcc).getHoldings("AAPL"));

        System.out.println("\n--- Order Book Matching Test ---");

        // Bob posts a SELL at $150
        broker.placeSellOrder(bobAcc, "AAPL", 10, new BigDecimal("150.00"));
        // Alice posts a BUY at $155 — crosses with Bob's $150 ask → TRADE!
        broker.placeBuyOrder(aliceAcc, "AAPL", 10, new BigDecimal("155.00"));

        System.out.println("\n--- No-Match Scenario ---");
        // Alice offers BUY at $140 but Bob asks $160 — no cross, both queue
        broker.placeBuyOrder(aliceAcc, "AAPL", 5, new BigDecimal("140.00"));
        broker.placeSellOrder(bobAcc, "AAPL", 5, new BigDecimal("160.00"));

        System.out.println("\n--- Final Balances ---");
        System.out.printf("Alice balance: $%s | AAPL held: %d%n",
                aliceAcc.getBalance(), broker.getPortfolio(aliceAcc).getHoldings("AAPL"));
        System.out.printf("Bob balance:   $%s | AAPL held: %d%n",
                bobAcc.getBalance(), broker.getPortfolio(bobAcc).getHoldings("AAPL"));
    }
}
