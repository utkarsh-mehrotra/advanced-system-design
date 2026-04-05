package musicstreamingservice_sde3;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AudioDispatcher {
    private final Map<String, Song> library = new ConcurrentHashMap<>();

    public void addSong(Song song) {
        library.put(song.getSongId(), song);
    }

    public void requestStream(StreamingSession session, String songId) {
        if (session.activate()) {
            Song s = library.get(songId);
            if (s != null) {
                System.out.println("AudioDispatcher: Authorized connection. Streaming " + songId);
                
                // Analytics detached allowing native read speeds to dominate
                EventBus.getInstance().publish("SONG_PLAYED", songId);
            } else {
                session.deactivate();
            }
        } else {
            System.out.println("AudioDispatcher: Stream already active for this session.");
        }
    }
}
