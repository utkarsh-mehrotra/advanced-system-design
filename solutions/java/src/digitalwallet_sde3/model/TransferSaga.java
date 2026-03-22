package digitalwallet_sde3.model;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * State Entity for a Distributed Cross-Account Transfer Transaction.
 */
public class TransferSaga {
    private final String sagaId;
    private final String fromAccountId;
    private final String toAccountId;
    private final BigDecimal amount;
    private SagaStatus status;
    private final long timestamp;

    public TransferSaga(String fromAccountId, String toAccountId, BigDecimal amount) {
        this.sagaId = UUID.randomUUID().toString();
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
        this.status = SagaStatus.PENDING;
        this.timestamp = System.currentTimeMillis();
    }

    public String getSagaId() { return sagaId; }
    public String getFromAccountId() { return fromAccountId; }
    public String getToAccountId() { return toAccountId; }
    public BigDecimal getAmount() { return amount; }
    public SagaStatus getStatus() { return status; }
    public void setStatus(SagaStatus status) { this.status = status; }
    public long getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return "Saga[" + sagaId.substring(0,8) + "]: " + fromAccountId + " -> " + toAccountId + " ($" + amount + ") | " + status;
    }
}
