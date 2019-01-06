package ch.bod.nfcMusic.sound;

import java.io.*;
import java.util.Vector;

import ch.bod.nfcMusic.Song;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.FactoryRegistry;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

public class Mp3Player {

    private AdvancedPlayer advancedPlayer;
    Vector<Song> songs = new Vector<Song>();
    int frame;

    public Mp3Player(Song intro) {
        addToPlaylist(intro);
        play();
    }

    public void playNext()
    {
        stop();
        if(songs.size() > 0)
        {
            songs.remove(0);
        }
        play();
    }

    public void play()
    {
        if(songs.size() > 0)
        {
            starteAbspielen(songs.get(0).getPath());
        }
    }

    public void addToPlaylist(Song song){
        songs.add(song);
    }

    private void starteAbspielen(final String dateiname) {
        try {
            playerVorbereiten(dateiname);
            Thread thread = new Thread() {
                public void run() {
                    try {
                        advancedPlayer.play();
                    } catch (JavaLayerException e) {
                        meldeProblem(dateiname);
                    } finally {
                        killPlayer();
                    }
                }
            };
            thread.start();

        } catch (Exception ex) {
            meldeProblem(dateiname);
        }
    }

    public void stop() {
        killPlayer();
    }

    private void playerVorbereiten(String dateiname) {
        try {
            InputStream is = gibEingabestream(dateiname);
            advancedPlayer = new AdvancedPlayer(is, erzeugeAudiogeraet());
            advancedPlayer.setPlayBackListener(new PlaybackListener() {
                @Override
                public void playbackFinished(PlaybackEvent evt) {
                    frame = evt.getFrame();
                }
            });
        } catch (IOException e) {
            meldeProblem(dateiname);
            killPlayer();
        } catch (JavaLayerException e) {
            meldeProblem(dateiname);
            killPlayer();
        }
    }

    private InputStream gibEingabestream(String dateiname)
            throws IOException {
        return new BufferedInputStream(
                new FileInputStream(dateiname));
    }

    private AudioDevice erzeugeAudiogeraet()
            throws JavaLayerException {
        return FactoryRegistry.systemRegistry().createAudioDevice();
    }

    private void killPlayer() {
        synchronized (this) {
            if (advancedPlayer != null) {
                advancedPlayer.stop();
                advancedPlayer = null;
            }
        }
    }

    private void meldeProblem(String dateiname) {
        System.out.println("Es gab ein Problem beim Abspielen von: " + dateiname);
    }

}