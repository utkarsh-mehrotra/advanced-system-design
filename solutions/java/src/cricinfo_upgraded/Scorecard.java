package cricinfo_upgraded;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Scorecard {
    private final String id;
    private final Match match;
    private final Map<String, AtomicInteger> teamScores;
    private final List<Innings> innings;

    public Scorecard(String id, Match match) {
        this.id = id;
        this.match = match;
        this.teamScores = new ConcurrentHashMap<>();
        this.innings = new CopyOnWriteArrayList<>();
    }

    public void incrementScore(String teamId, int runsToAdd) {
        // computeIfAbsent handles concurrent first-time additions safely
        teamScores.computeIfAbsent(teamId, k -> new AtomicInteger(0))
                  .addAndGet(runsToAdd);
    }

    public void addInnings(Innings inning) {
        this.innings.add(inning); // Using CopyOnWriteArrayList, thread-safe
    }

    public String getId() {
        return id;
    }

    public Match getMatch() {
        return match;
    }

    public Map<String, AtomicInteger> getTeamScores() {
        return teamScores;
    }

    public List<Innings> getInnings() {
        return innings;
    }
}
