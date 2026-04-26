package cricinfo_sde3;

import java.util.List;

public class CricinfoFacade {
    private final MatchService matchService;
    private final ScorecardService scorecardService;

    public CricinfoFacade(MatchService matchService, ScorecardService scorecardService) {
        this.matchService = matchService;
        this.scorecardService = scorecardService;
    }

    public void addMatch(Match match) {
        matchService.addMatch(match);
    }

    public Match getMatch(String matchId) {
        return matchService.getMatch(matchId);
    }

    public List<Match> getAllMatches() {
        return matchService.getAllMatches();
    }

    public void updateMatchStatus(String matchId, MatchStatus status) {
        matchService.updateMatchStatus(matchId, status);
    }

    public void createScorecard(Match match) {
        scorecardService.createScorecard(match);
    }

    public Scorecard getScorecard(String scorecardId) {
        return scorecardService.getScorecard(scorecardId);
    }

    public void addRunsToScore(String scorecardId, String teamId, int runsToAdd) {
        scorecardService.updateScore(scorecardId, teamId, runsToAdd);
    }

    public void recordInnings(String scorecardId, Innings innings) {
        scorecardService.addInnings(scorecardId, innings);
    }
}
