package onlinestockbrokeragesystem_sde3.model.events;

import onlinestockbrokeragesystem_sde3.model.Trade;
import java.util.UUID;

public class TradeExecutedEvent implements Event {
    private final String eventId;
    private final long timestamp;
    private final Trade trade;

    public TradeExecutedEvent(Trade trade) {
        this.eventId = UUID.randomUUID().toString();
        this.timestamp = System.currentTimeMillis();
        this.trade = trade;
    }

    @Override
    public String getEventId() { return eventId; }
    
    @Override
    public long getTimestamp() { return timestamp; }

    public Trade getTrade() { return trade; }
}
