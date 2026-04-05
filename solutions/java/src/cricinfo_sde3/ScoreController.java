package cricinfo_sde3;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class ScoreController {
    private final Map<String, Match> matches = new ConcurrentHashMap<>();
    private final Map<String, ReentrantLock> matchLocks = new ConcurrentHashMap<>();

    public void registerMatch(Match match) {
        matches.put(match.getMatchId(), match);
        matchLocks.put(match.getMatchId(), new ReentrantLock());
    }

    public void updateScore(String matchId, int runs) {
        ReentrantLock lock = matchLocks.get(matchId);
        if (lock != null) {
            // Write path is guarded by ReentrantLock avoiding lost updates from multiple umpires/scorers
            lock.lock();
            try {
                Match m = matches.get(matchId);
                m.incrementScore(runs);
                EventBus.getInstance().publish("LIVE_SCORE_UPDATE", matchId + ":" + m.getCurrentScore());
            } finally {
                lock.unlock();
            }
        }
    }
}
