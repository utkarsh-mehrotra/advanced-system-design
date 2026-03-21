package cricinfo_upgraded;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Match {
    private final String id;
    private final String title;
    private final String venue;
    private final LocalDateTime startTime;
    private final List<Team> teams;
    private final AtomicReference<MatchStatus> status;

    public Match(String id, String title, String venue, LocalDateTime startTime, List<Team> teams) {
        this.id = id;
        this.title = title;
        this.venue = venue;
        this.startTime = startTime;
        this.teams = List.copyOf(teams);
        this.status = new AtomicReference<>(MatchStatus.SCHEDULED); // Thread-safe status
    }

    public String getId() {
        return id;
    }

    public void setStatus(MatchStatus newStatus) {
        this.status.set(newStatus);
    }

    public MatchStatus getStatus() {
        return this.status.get();
    }

    public String getTitle() {
        return title;
    }
}
