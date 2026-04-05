package musicstreamingservice_sde3;

public class AccountMetadata {
    public AccountMetadata() {
        EventBus.getInstance().subscribe("SONG_PLAYED", this::updatePlayCount);
    }

    private void updatePlayCount(Object payload) {
        System.out.println("Analytics [Async Worker]: Registered play for song -> " + payload);
    }
}
