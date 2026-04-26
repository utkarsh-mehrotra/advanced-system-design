package cricinfo_sde3;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ScorecardServiceImpl implements ScorecardService {
    private final Map<String, Scorecard> scorecards;
    private final AtomicInteger scorecardIdCounter;

    public ScorecardServiceImpl() {
        scorecards = new ConcurrentHashMap<>();
        scorecardIdCounter = new AtomicInteger(0);
    }

    @Override
    public void createScorecard(Match match) {
        String scorecardId = generateScorecardId(match.getId());
        Scorecard scorecard = new Scorecard(scorecardId, match);
        scorecards.put(scorecardId, scorecard);
    }

    @Override
    public Scorecard getScorecard(String scorecardId) {
        return scorecards.get(scorecardId);
    }

    @Override
    public void updateScore(String scorecardId, String teamId, int runsToAdd) {
        Scorecard scorecard = scorecards.get(scorecardId);
        if (scorecard != null) {
            scorecard.incrementScore(teamId, runsToAdd); // Thread-safe down to the map/integer level
        }
    }

    @Override
    public void addInnings(String scorecardId, Innings innings) {
        Scorecard scorecard = scorecards.get(scorecardId);
        if (scorecard != null) {
            scorecard.addInnings(innings);
        }
    }

    private String generateScorecardId(String matchId) {
        int scorecardId = scorecardIdCounter.incrementAndGet();
        return "SC-" + matchId + "-" + String.format("%04d", scorecardId);
    }
}
