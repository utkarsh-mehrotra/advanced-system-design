package onlinestockbrokeragesystem_sde3.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Value object representing an executed trade.
 */
public class Trade {
    private final String tradeId;
    private final Order buyOrder;
    private final Order sellOrder;
    private final String symbol;
    private final int quantity;
    private final BigDecimal price;
    private final Instant executedAt;

    public Trade(Order buyOrder, Order sellOrder, String symbol, int quantity, BigDecimal price) {
        this.tradeId = UUID.randomUUID().toString();
        this.buyOrder = buyOrder;
        this.sellOrder = sellOrder;
        this.symbol = symbol;
        this.quantity = quantity;
        this.price = price;
        this.executedAt = Instant.now();
    }

    public String getTradeId() { return tradeId; }
    public Order getBuyOrder() { return buyOrder; }
    public Order getSellOrder() { return sellOrder; }
    public String getSymbol() { return symbol; }
    public int getQuantity() { return quantity; }
    public BigDecimal getPrice() { return price; }
    public Instant getExecutedAt() { return executedAt; }

    @Override
    public String toString() {
        return "Trade{" + symbol + ": " + quantity + " @ $" + price + " [Buy: " + buyOrder.getAccountId() + ", Sell: " + sellOrder.getAccountId() + "]}";
    }
}
