package cricinfo_upgraded;

public interface ScorecardService {
    void createScorecard(Match match);
    Scorecard getScorecard(String scorecardId);
    void updateScore(String scorecardId, String teamId, int runsToAdd);
    void addInnings(String scorecardId, Innings innings);
}
