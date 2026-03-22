package onlinestockbrokeragesystem_sde3.service;

import onlinestockbrokeragesystem_sde3.messaging.MessageBus;
import onlinestockbrokeragesystem_sde3.model.Order;
import onlinestockbrokeragesystem_sde3.model.OrderSide;
import onlinestockbrokeragesystem_sde3.model.events.OrderCreatedEvent;

import java.math.BigDecimal;

/**
 * Stateless external entry point.
 * Validates orders, pre-authorizes assets with the Ledger, and asynchronously pushes to the matching topic.
 */
public class OrderGatewayService {
    private final LedgerService ledgerService;

    public OrderGatewayService(LedgerService ledgerService) {
        this.ledgerService = ledgerService;
    }

    public Order placeOrder(String accountId, String symbol, OrderSide side, int quantity, BigDecimal limitPrice) {
        if (side == OrderSide.BUY) {
            BigDecimal cost = limitPrice.multiply(BigDecimal.valueOf(quantity));
            if (!ledgerService.reserveFunds(accountId, cost)) {
                System.out.println("[GATEWAY] BUY Order rejected: Insufficient funds for " + accountId);
                return null;
            }
        } else {
            if (!ledgerService.reservePosition(accountId, symbol, quantity)) {
                System.out.println("[GATEWAY] SELL Order rejected: Insufficient stock (" + symbol + ") for " + accountId);
                return null;
            }
        }

        Order order = new Order(accountId, symbol, side, quantity, limitPrice);
        System.out.println("[GATEWAY] Accepted: " + order);

        // Put on the event bus and return immediately (Asynchronous pipeline)
        MessageBus.getInstance().publishOrder(new OrderCreatedEvent(order));
        
        return order;
    }
}
