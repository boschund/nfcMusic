package ch.bod.nfcMusic.gui;

import javax.swing.JFrame;

import ch.bod.nfcMusic.sound.Playlist;
import ch.bod.nfcMusic.sound.Song;


public abstract class AbstractMusicGUI extends JFrame
{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public abstract void start();
    
    public abstract void updatePlaylist(Playlist playlist);
    
    public abstract void setReadable();

    public abstract void setWritable();

    public abstract void setClean();

    public abstract void setOutput(String next);

    public abstract void setActualSong(Song next);

    public abstract String getInput();
    
    public abstract void playButton();

    public abstract void ffButton();

    public abstract void pauseButton();

}
