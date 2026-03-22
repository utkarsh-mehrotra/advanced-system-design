package musicstreamingservice_sde2;

import java.util.List;

public class MusicStreamingFacade {
    private final MusicLibrary musicLibrary;
    private final UserManager userManager;
    private final MusicRecommender musicRecommender;
    private final MusicPlayer musicPlayer;

    public MusicStreamingFacade() {
        musicLibrary = new MusicLibrary();
        userManager = new UserManager();
        musicRecommender = new MusicRecommender();
        musicPlayer = new MusicPlayer();
    }

    public MusicLibrary getMusicLibrary() {
        return musicLibrary;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public MusicRecommender getMusicRecommender() {
        return musicRecommender;
    }

    public MusicPlayer getMusicPlayer() {
        return musicPlayer;
    }

    public List<Song> searchMusic(String query) {
        return musicLibrary.searchSongs(query);
    }

    public void shutdown() {
        musicPlayer.shutdown();
    }
}
