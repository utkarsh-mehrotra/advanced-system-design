package cricinfo_sde3;

public class LiveScoreWebSocket {
    public LiveScoreWebSocket() {
        EventBus.getInstance().subscribe("LIVE_SCORE_UPDATE", this::broadcastScore);
    }

    private void broadcastScore(Object payload) {
        System.out.println("LiveScoreWebSocket [Fan-Out]: Streaming to 1M+ active connections -> " + payload);
    }
}
