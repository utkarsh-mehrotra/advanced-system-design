package musicstreamingservice_sde3;

public class MusicDemoSDE3 {
    public static void main(String[] args) {
        new AccountMetadata(); // Boot analytics 

        AudioDispatcher dispatcher = new AudioDispatcher();
        dispatcher.addSong(new Song("TRACK_01", "/data/lossless/t01.flac"));

        StreamingSession s1 = new StreamingSession("SES_XYZ");
        
        System.out.println("Initiating stream...");
        dispatcher.requestStream(s1, "TRACK_01");
        
        System.out.println("Attempting concurrent stream conflict...");
        dispatcher.requestStream(s1, "TRACK_01");
    }
}
