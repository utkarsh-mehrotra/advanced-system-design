package digitalwallet_sde3.model.events;

import java.math.BigDecimal;
import java.util.UUID;

public class TransferRequestedEvent implements Event {
    private final String eventId = UUID.randomUUID().toString();
    private final String sagaId;
    private final String fromAccountId;
    private final String toAccountId;
    private final BigDecimal amount;
    private final long timestamp = System.currentTimeMillis();

    public TransferRequestedEvent(String sagaId, String fromAccountId, String toAccountId, BigDecimal amount) {
        this.sagaId = sagaId;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
    }

    @Override public String getEventId() { return eventId; }
    @Override public String getSagaId() { return sagaId; }
    @Override public long getTimestamp() { return timestamp; }
    public String getFromAccountId() { return fromAccountId; }
    public String getToAccountId() { return toAccountId; }
    public BigDecimal getAmount() { return amount; }
}
