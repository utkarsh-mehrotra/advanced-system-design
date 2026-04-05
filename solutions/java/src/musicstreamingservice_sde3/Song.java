package musicstreamingservice_sde3;

public class Song {
    private final String songId;
    private final String filePath;

    public Song(String songId, String filePath) {
        this.songId = songId;
        this.filePath = filePath;
    }

    public String getSongId() { return songId; }
    public String getFilePath() { return filePath; }
}
