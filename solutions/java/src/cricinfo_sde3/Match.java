package cricinfo_sde3;

public class Match {
    private final String matchId;
    private volatile int currentScore; // Volatile for ultra-fast lock-free reads
    private volatile MatchState state;

    public Match(String matchId) {
        this.matchId = matchId;
        this.currentScore = 0;
        this.state = MatchState.UPCOMING;
    }

    public String getMatchId() { return matchId; }
    public int getCurrentScore() { return currentScore; }
    public MatchState getState() { return state; }

    // Package-private. Only ScoreController holds write locks
    void incrementScore(int runs) {
        this.currentScore += runs;
    }

    void setState(MatchState state) {
        this.state = state;
    }
}
