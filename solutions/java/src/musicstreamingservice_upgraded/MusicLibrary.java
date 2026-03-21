package musicstreamingservice_upgraded;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class MusicLibrary {
    private final Map<String, Song> songs;
    private final Map<String, Album> albums;
    private final Map<String, Artist> artists;

    public MusicLibrary() {
        this.songs = new ConcurrentHashMap<>();
        this.albums = new ConcurrentHashMap<>();
        this.artists = new ConcurrentHashMap<>();
    }

    public void addSong(Song song) {
        songs.put(song.getId(), song);
    }

    public void addAlbum(Album album) {
        albums.put(album.getId(), album);
        for (Song song : album.getSongs()) {
            addSong(song);
        }
    }

    public void addArtist(Artist artist) {
        artists.put(artist.getId(), artist);
        for (Album album : artist.getAlbums()) {
            addAlbum(album);
        }
    }

    public Song getSong(String songId) {
        return songs.get(songId);
    }

    public Album getAlbum(String albumId) {
        return albums.get(albumId);
    }

    public Artist getArtist(String artistId) {
        return artists.get(artistId);
    }

    public List<Song> searchSongs(String query) {
        String queryLower = query.toLowerCase();
        // Utilizing parallel stream for fast O(N) catalog search across multiple cores
        return songs.values().parallelStream()
                .filter(song -> song.getTitle().toLowerCase().contains(queryLower) ||
                                song.getArtist().toLowerCase().contains(queryLower) ||
                                song.getAlbum().toLowerCase().contains(queryLower))
                .collect(Collectors.toList());
    }
}
