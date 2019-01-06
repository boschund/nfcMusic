package ch.bod.nfcMusic.sound;

import java.io.FileInputStream;

import ch.bod.nfcMusic.MusicController;
import ch.bod.nfcMusic.Song;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.FactoryRegistry;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

import static ch.bod.nfcMusic.MusicController.*;

public class ThreadedMp3Player extends Thread {

    EventListener listener = new EventListener();
    AudioDevice audioDevice;
    AdvancedPlayer player;
    MusicController controller;

    volatile Song playingMP3;
    public volatile boolean queued = false;
    public volatile boolean stopped = false;

    public volatile boolean kill = false;
    public volatile boolean playing = false;
    public volatile boolean paused = false;
    volatile int pauseFrames = 0;

    public ThreadedMp3Player(MusicController musicController) {
        setDaemon(true);
        setName("mp3 Player Thread");
        controller = musicController;
        try {
            audioDevice =  FactoryRegistry.systemRegistry().createAudioDevice();
        } catch (JavaLayerException e) {
            controller.error(e);
        }
    }

    @Override
    public void run() {
        try {
            while(!kill ) {
                if (!stopped) {
                    if (queued && playingMP3 != null) {
                        if (player != null)
                            resetPlayer();
                        player = new AdvancedPlayer(new FileInputStream(playingMP3.getFile()));
                        player.setPlayBackListener(listener);
                        queued = false;
                    }

                    boolean played = false;

                    if (player != null && audioDevice != null) {
                        if (pauseFrames == 0) {
                            controller.info("Playing File");
                            player.play();
                            playing = true;
                        } else if (!paused) {
                            controller.info("Was paused, restarting at " + pauseFrames);
                            int startFrame = pauseFrames;
                            pauseFrames = 0;
                            player.play(startFrame, Integer.MAX_VALUE);
                            playing = true;
                        }
                        played = true;
                    }

                    if (played && !paused && !queued) {
                        controller.info("Song done, next...");
                        play();
                    }
                }
            }
        } catch(Exception e) {
            controller.error(e);
        }
    }

    private Song getNextSong()
    {
        return controller.getNext();
    }

    public void resetPlayer() {
        controller.info("Resetting Player.");

        playing = false;
        if(player != null)
            player.close();
        player = null;
    }

    public void play() {
        resetPlayer();
        stopped = false;
        playingMP3 = getNextSong();
        paused = false;
        queued = true;
    }

    public void setPlayingSong(Song song) {
        playingMP3 = song;
    }

    public Song getPlayingSong() {
        return playingMP3;
    }

    public int getFramesPlayed() {
        return player == null ? 0 : pauseFrames;
    }

    public float getFractionPlayed() {
        int pFrames = getFramesPlayed();
        int tFrames = playingMP3 == null ? 0 : pauseFrames;
        if(pFrames == 0 || tFrames == 0)
            return 0F;

        return (float) pFrames / (float) tFrames;
    }

    public void forceKill() {
        try {
            resetPlayer();
            interrupt();

            finalize();
            kill = true;
        } catch(Throwable e) {
            controller.error(e);
        }
    }

    class EventListener extends PlaybackListener {

        @Override
        public void playbackFinished(PlaybackEvent evt) {
            if(paused) {
                pauseFrames = evt.getFrame();
                controller.info("Paused at " + pauseFrames);
            }
        }
    }
}
