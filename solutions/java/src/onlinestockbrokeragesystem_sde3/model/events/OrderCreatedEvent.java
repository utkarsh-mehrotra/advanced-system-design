package onlinestockbrokeragesystem_sde3.model.events;

import onlinestockbrokeragesystem_sde3.model.Order;
import java.util.UUID;

public class OrderCreatedEvent implements Event {
    private final String eventId;
    private final long timestamp;
    private final Order order;

    public OrderCreatedEvent(Order order) {
        this.eventId = UUID.randomUUID().toString();
        this.timestamp = System.currentTimeMillis();
        this.order = order;
    }

    @Override
    public String getEventId() { return eventId; }
    
    @Override
    public long getTimestamp() { return timestamp; }

    public Order getOrder() { return order; }
}
