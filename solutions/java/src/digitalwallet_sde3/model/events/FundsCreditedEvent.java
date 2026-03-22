package digitalwallet_sde3.model.events;

import java.math.BigDecimal;
import java.util.UUID;

public class FundsCreditedEvent implements Event {
    private final String eventId = UUID.randomUUID().toString();
    private final String sagaId;
    private final String toAccountId;
    private final BigDecimal amount;
    private final long timestamp = System.currentTimeMillis();

    public FundsCreditedEvent(String sagaId, String toAccountId, BigDecimal amount) {
        this.sagaId = sagaId;
        this.toAccountId = toAccountId;
        this.amount = amount;
    }

    @Override public String getEventId() { return eventId; }
    @Override public String getSagaId() { return sagaId; }
    @Override public long getTimestamp() { return timestamp; }
    public String getToAccountId() { return toAccountId; }
    public BigDecimal getAmount() { return amount; }
}
