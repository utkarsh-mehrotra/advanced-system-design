package musicstreamingservice_sde3;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

public class MusicPlayer {
    private final ExecutorService executorService;
    private Song currentSong;
    private int currentPlaybackTimeSecs;
    private final AtomicBoolean isPlaying;
    private final AtomicBoolean isStopped;
    private Future<?> playbackFuture;

    public MusicPlayer() {
        this.executorService = Executors.newSingleThreadExecutor();
        this.isPlaying = new AtomicBoolean(false);
        this.isStopped = new AtomicBoolean(true);
    }

    public void playSong(Song song) {
        stopSong(); // Clean up existing if any
        currentSong = song;
        currentPlaybackTimeSecs = 0;
        isPlaying.set(true);
        isStopped.set(false);
        System.out.println("Started playing: " + song.getTitle());

        playbackFuture = executorService.submit(this::playbackLoop);
    }

    public void pauseSong() {
        if (!isStopped.get()) {
            isPlaying.set(false);
            System.out.println("Paused: " + currentSong.getTitle() + " at " + currentPlaybackTimeSecs + "s");
        }
    }

    public void resumeSong() {
        if (!isStopped.get() && !isPlaying.get()) {
            isPlaying.set(true);
            System.out.println("Resumed: " + currentSong.getTitle() + " from " + currentPlaybackTimeSecs + "s");
        }
    }

    public void stopSong() {
        if (!isStopped.get()) {
            System.out.println("Stopped: " + currentSong.getTitle());
            isPlaying.set(false);
            isStopped.set(true);
            if (playbackFuture != null) {
                playbackFuture.cancel(true); // Terminate loop early
            }
        }
    }

    public void seekTo(int timeSecs) {
        if (currentSong != null && timeSecs >= 0 && timeSecs <= currentSong.getDurationSecs()) {
            currentPlaybackTimeSecs = timeSecs;
            System.out.println("Seeked to: " + timeSecs + "s");
        }
    }

    private void playbackLoop() {
        try {
            while (!isStopped.get() && currentPlaybackTimeSecs < currentSong.getDurationSecs()) {
                if (isPlaying.get()) {
                    currentPlaybackTimeSecs++;
                    // System.out.println("Playing " + currentSong.getTitle() + " ... " + currentPlaybackTimeSecs + "s");
                }
                Thread.sleep(100); // Wait 100ms per simulated second instead of 1000ms for faster tests
            }
            if (!isStopped.get()) {
                System.out.println("Finished playing: " + currentSong.getTitle());
                isPlaying.set(false);
                isStopped.set(true);
            }
        } catch (InterruptedException e) {
            System.out.println("Playback thread interrupted.");
            Thread.currentThread().interrupt();
        }
    }
    
    public void shutdown() {
        stopSong();
        executorService.shutdownNow();
    }
}
