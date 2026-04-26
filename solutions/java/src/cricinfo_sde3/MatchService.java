package cricinfo_sde3;

import java.util.List;

public interface MatchService {
    void addMatch(Match match);
    Match getMatch(String matchId);
    List<Match> getAllMatches();
    void updateMatchStatus(String matchId, MatchStatus status);
}
