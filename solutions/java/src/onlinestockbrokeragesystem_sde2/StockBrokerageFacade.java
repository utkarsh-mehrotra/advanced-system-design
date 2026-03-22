package onlinestockbrokeragesystem_sde2;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StockBrokerageFacade {
    private final Map<String, Account> accounts = new ConcurrentHashMap<>();
    private final Map<String, Portfolio> portfolios = new ConcurrentHashMap<>();
    // One OrderMatchingEngine per stock symbol
    private final Map<String, OrderMatchingEngine> orderBooks = new ConcurrentHashMap<>();

    public Account createAccount(User user, BigDecimal initialBalance) {
        Account acc = new Account("ACC-" + user.getId(), user, initialBalance);
        accounts.put(acc.getId(), acc);
        portfolios.put(acc.getId(), new Portfolio());
        return acc;
    }

    private OrderMatchingEngine getOrCreateEngine(String symbol) {
        return orderBooks.computeIfAbsent(symbol, OrderMatchingEngine::new);
    }

    public Order placeBuyOrder(Account account, String symbol, int quantity, BigDecimal limitPrice) {
        Portfolio portfolio = portfolios.get(account.getId());
        Order order = new Order(account, portfolio, symbol, OrderSide.BUY, quantity, limitPrice);
        System.out.printf("[SUBMIT BUY]  %s: %d shares @ $%s by %s%n",
                symbol, quantity, limitPrice.toPlainString(), account.getUser().getName());
        getOrCreateEngine(symbol).submitOrder(order);
        return order;
    }

    public Order placeSellOrder(Account account, String symbol, int quantity, BigDecimal limitPrice) {
        Portfolio portfolio = portfolios.get(account.getId());
        Order order = new Order(account, portfolio, symbol, OrderSide.SELL, quantity, limitPrice);
        System.out.printf("[SUBMIT SELL] %s: %d shares @ $%s by %s%n",
                symbol, quantity, limitPrice.toPlainString(), account.getUser().getName());
        getOrCreateEngine(symbol).submitOrder(order);
        return order;
    }

    public Portfolio getPortfolio(Account account) {
        return portfolios.get(account.getId());
    }
}
