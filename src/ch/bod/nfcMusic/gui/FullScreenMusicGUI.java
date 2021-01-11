package ch.bod.nfcMusic.gui;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.Toolkit;
import java.io.File;

import javax.swing.JFrame;

import ch.bod.nfcMusic.MusicController;
import ch.bod.nfcMusic.sound.Playlist;
import ch.bod.nfcMusic.sound.Song;


public class FullScreenMusicGUI extends AbstractMusicGUI
{
	private static final long serialVersionUID = 1L;
	private MusicController controller;
    MusicGUI center = null;

    private MusicController.MODE mode;

    public FullScreenMusicGUI(MusicController controller, MusicController.MODE mode)
    {
        this.controller = controller;
        this.mode = mode;
        // Remove window title and borders
        setUndecorated(true);
        // Make frame topmost
        setAlwaysOnTop(true);
        // Disable Alt+F4 on Windows
        //setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    }

    public void start() {
    	
        try {
            // setting the frame. best: 700 x 400, kioskmode: fullscreen
        	setExtendedState(Frame.MAXIMIZED_BOTH);
        	setTitle("NFCMusic");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);
            setIconImage(Toolkit.getDefaultToolkit().getImage(controller.getReferencePath() + File.separator + "resources" + File.separator + "metal.png"));

            center = new MusicGUI(controller, mode);
            
            //creating GUI
            createGUI();

        } catch (Exception e){
            controller.error(e);
        }
    }

    private void createGUI() {
        setLayout(new BorderLayout());
        
        Panel start = new Panel();
        start.setSize(20, 400);
        start.setVisible(true);
        add(start, BorderLayout.NORTH);
        
        Panel left = new Panel();
        left.setSize(400, 20);
        left.setVisible(true);
        add(left, BorderLayout.WEST);
        
        Panel right = new Panel();
        right.setSize(400, 20);
        right.setVisible(true);
        add(right, BorderLayout.EAST);
        
        Panel end = new Panel();
        end.setSize(20, 400);
        end.setVisible(true);
        add(end, BorderLayout.EAST);
        
        add(center, BorderLayout.EAST);

        repaint();
        setVisible(true);
    }
    

    public void updatePlaylist(Playlist playlist) {
        center.updatePlaylist(playlist);
    }

    public void setReadable()
    {
    	center.setReadable();
    }

    public void setWritable()
    {
    	center.setWritable();
    }

    public void setClean()
    {
    	center.setClean();
    }

    public void setOutput(String next)
    {
    	center.setOutput(next);
    }

    public void playButton()
    {
    	center.playButton();
    }

    public void ffButton()
    {
    	center.ffButton();
    }

    public void pauseButton()
    {
    	center.pauseButton();
    }

    public void setActualSong(Song next)
    {
    	center.setActualSong(next);
    }

    public String getInput()
    {
    	return center.getInput();
    }

}
