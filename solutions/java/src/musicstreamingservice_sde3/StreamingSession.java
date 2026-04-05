package musicstreamingservice_sde3;

import java.util.concurrent.atomic.AtomicBoolean;

public class StreamingSession {
    private final String sessionId;
    private final AtomicBoolean isActive;

    public StreamingSession(String sessionId) {
        this.sessionId = sessionId;
        this.isActive = new AtomicBoolean(false);
    }

    public boolean activate() {
        return isActive.compareAndSet(false, true);
    }

    public void deactivate() {
        isActive.set(false);
    }

    public boolean getStatus() { return isActive.get(); }
}
