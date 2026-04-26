package musicstreamingservice_sde3;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MusicRecommender {
    private final Map<String, List<Song>> userRecommendations;

    public MusicRecommender() {
        userRecommendations = new ConcurrentHashMap<>();
    }

    public void updateRecommendations(String userId, List<Song> recommendedSongs) {
        userRecommendations.put(userId, recommendedSongs);
    }

    public List<Song> recommendSongs(User user) {
        return userRecommendations.getOrDefault(user.getId(), new ArrayList<>());
    }
}
