package onlinestockbrokeragesystem_sde3.service;

import onlinestockbrokeragesystem_sde3.messaging.MessageBus;
import onlinestockbrokeragesystem_sde3.model.Account;
import onlinestockbrokeragesystem_sde3.model.Order;
import onlinestockbrokeragesystem_sde3.model.OrderSide;
import onlinestockbrokeragesystem_sde3.model.Portfolio;
import onlinestockbrokeragesystem_sde3.model.Trade;
import onlinestockbrokeragesystem_sde3.model.events.TradeExecutedEvent;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Service responsible for all balances and portfolios.
 * Consumes TradeExecutedEvents to settle ledger state asynchronously.
 */
public class LedgerService {
    private final ConcurrentHashMap<String, Account> accounts = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Portfolio> portfolios = new ConcurrentHashMap<>();
    private final ExecutorService consumerThread = Executors.newSingleThreadExecutor();

    public LedgerService() {
        startTradeConsumer();
    }

    public Account createAccount(String accountId, BigDecimal initialBalance) {
        Account account = new Account(accountId, initialBalance);
        accounts.put(accountId, account);
        portfolios.put(accountId, new Portfolio(accountId));
        return account;
    }

    public boolean reserveFunds(String accountId, BigDecimal amount) {
        Account account = accounts.get(accountId);
        if (account == null) return false;
        return account.reserveFunds(amount);
    }

    public boolean reservePosition(String accountId, String symbol, int quantity) {
        Portfolio portfolio = portfolios.get(accountId);
        if (portfolio == null) return false;
        return portfolio.reservePosition(symbol, quantity);
    }

    private void startTradeConsumer() {
        consumerThread.submit(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    TradeExecutedEvent event = MessageBus.getInstance().subscribeToTradesOutbound().take();
                    settleTrade(event.getTrade());
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }

    private void settleTrade(Trade trade) {
        Order buyOrder = trade.getBuyOrder();
        Order sellOrder = trade.getSellOrder();

        BigDecimal settlementAmount = trade.getPrice().multiply(BigDecimal.valueOf(trade.getQuantity()));

        // Buyer: Commit reserved funds, add stock to portfolio
        Account buyer = accounts.get(buyOrder.getAccountId());
        Portfolio buyerPortfolio = portfolios.get(buyOrder.getAccountId());
        
        // Let's release the remaining locked funds if the order price was higher than the trade price (price improvement).
        // For simplicity in this demo, we assume the reserved amount was tradePrice * tradeQty.
        // In reality, the gateway locks limitPrice * qty. We commit what was traded, releasing the diff on completion.
        buyer.commitReservedFunds(settlementAmount);
        // If trade price is better than limit price, we should refund the difference. 
        // Difference = (buy limit - trade price) * qty
        BigDecimal priceImprovement = buyOrder.getLimitPrice().subtract(trade.getPrice()).multiply(BigDecimal.valueOf(trade.getQuantity()));
        if (priceImprovement.compareTo(BigDecimal.ZERO) > 0) {
             buyer.releaseReservedFunds(priceImprovement); 
        }

        buyerPortfolio.addPosition(trade.getSymbol(), trade.getQuantity());

        // Seller: Commit reserved stock, add funds to account
        Account seller = accounts.get(sellOrder.getAccountId());
        Portfolio sellerPortfolio = portfolios.get(sellOrder.getAccountId());
        
        sellerPortfolio.commitReservedPosition(trade.getSymbol(), trade.getQuantity());
        seller.creditFunds(settlementAmount);

        System.out.println("[LEDGER:SETTLED] Trade " + trade.getTradeId().substring(0,8) + 
            " -> Sent $" + settlementAmount + " to " + seller.getId() + " | Given " + trade.getQuantity() + " shares of " + trade.getSymbol() + " to " + buyer.getId());
    }

    public Account getAccount(String accountId) {
        return accounts.get(accountId);
    }

    public Portfolio getPortfolio(String accountId) {
        return portfolios.get(accountId);
    }
    
    public void shutdown() {
        consumerThread.shutdownNow();
    }
}
