package digitalwallet_sde3.model;

public enum SagaStatus {
    PENDING,
    RESERVED,
    CREDITED,
    COMPLETED,
    FAILED_TO_RESERVE,
    ROLLBACK_INITIATED,
    ROLLED_BACK
}
