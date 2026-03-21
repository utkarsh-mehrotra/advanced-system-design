package musicstreamingservice_upgraded;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Playlist {
    private final String id;
    private final String name;
    private final String ownerId;
    private final List<Song> songs;

    public Playlist(String id, String name, String ownerId) {
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
        this.songs = new CopyOnWriteArrayList<>();
    }

    public void addSong(Song song) {
        songs.add(song);
    }

    public void removeSong(Song song) {
        songs.remove(song);
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getOwnerId() { return ownerId; }
    public List<Song> getSongs() { return songs; }
}
