package musicstreamingservice_sde3;

import java.util.List;

public class MusicStreamingServiceDemoUpgraded {
    public static void run() {
        MusicStreamingFacade streamingService = new MusicStreamingFacade();

        // Create songs
        Song song1 = new Song("S1", "Bohemian Rhapsody", "Queen", "A Night at the Opera", 10); // Simulated 10s for fast test
        Song song2 = new Song("S2", "Stairway to Heaven", "Led Zeppelin", "Led Zeppelin IV", 15);
        
        Album album1 = new Album("AL1", "A Night at the Opera", "Queen", List.of(song1));
        Artist artist1 = new Artist("AR1", "Queen", List.of(album1));
        
        streamingService.getMusicLibrary().addArtist(artist1);
        streamingService.getMusicLibrary().addSong(song2); // Independent song

        // Search using parallel stream
        System.out.println("Searching for 'heaven'...");
        List<Song> results = streamingService.searchMusic("heaven");
        for (Song s : results) {
            System.out.println("Found: " + s.getTitle() + " by " + s.getArtist());
        }

        // Playback test
        MusicPlayer player = streamingService.getMusicPlayer();
        
        System.out.println("\n--- Testing Playback Engine ---");
        player.playSong(song1);
        
        try {
            Thread.sleep(300); // Wait 3s simulating playback
            player.pauseSong();
            Thread.sleep(200);
            player.seekTo(8);
            player.resumeSong();
            Thread.sleep(400); // Let it finish naturally
            
            System.out.println("\n--- Starting second song ---");
            player.playSong(song2);
            Thread.sleep(200);
            player.stopSong();
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            streamingService.shutdown();
        }
    }

    public static void main(String[] args) {
        run();
    }
}
