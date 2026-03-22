package cricinfo_sde2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MatchServiceImpl implements MatchService {
    private final Map<String, Match> matches;

    public MatchServiceImpl() {
        matches = new ConcurrentHashMap<>();
    }

    @Override
    public void addMatch(Match match) {
        matches.put(match.getId(), match);
    }

    @Override
    public Match getMatch(String matchId) {
        return matches.get(matchId);
    }

    @Override
    public List<Match> getAllMatches() {
        return new ArrayList<>(matches.values());
    }

    @Override
    public void updateMatchStatus(String matchId, MatchStatus status) {
        Match match = matches.get(matchId);
        if (match != null) {
            match.setStatus(status); // Thread-safe via AtomicReference internally
        }
    }
}
