package musicstreamingservice_upgraded;

public class Song {
    private final String id;
    private final String title;
    private final String artist;
    private final String album;
    private final int durationSecs;

    public Song(String id, String title, String artist, String album, int durationSecs) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.durationSecs = durationSecs;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getArtist() { return artist; }
    public String getAlbum() { return album; }
    public int getDurationSecs() { return durationSecs; }

    @Override
    public String toString() {
        return title + " - " + artist;
    }
}
