package digitalwallet_sde3.model.events;

public interface Event {
    String getEventId();
    String getSagaId();
    long getTimestamp();
}
