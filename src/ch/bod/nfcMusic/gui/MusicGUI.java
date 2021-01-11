package ch.bod.nfcMusic.gui;

import ch.bod.nfcMusic.MusicController;
import ch.bod.nfcMusic.sound.Playlist;
import ch.bod.nfcMusic.sound.Song;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;


public class MusicGUI extends JPanel
{
	private static final long serialVersionUID = 1L;
	private SimpleTable commingSoon;
    private ImagePanel playing;
    private PlayPanel playButton;
    private final JProgressBar loader = new JProgressBar();
    private MusicController controller;
    private FooterPanel footerPanel = new FooterPanel();

    private MusicController.MODE mode;

    public MusicGUI(MusicController controller, MusicController.MODE mode)
    {
        this.controller = controller;
        this.mode = mode;
    }

    public void start() {
        try {
            
            //creating GUI
            createGUI();

        } catch (Exception e){
            controller.error(e);
        }
    }

    private void createGUI() {
        setLayout(new BorderLayout());

        // progressLoader @ header
        loader.setValue(0);
        loader.setStringPainted(true);
        loader.setIndeterminate(true);
        add(loader, BorderLayout.PAGE_START);
        loader.setVisible(true);
        loader.setStringPainted(false);

        //FooterPanel Buttons
        footerPanel.create(controller, mode);
        add(footerPanel, BorderLayout.PAGE_END);
        footerPanel.setVisible(true);

        // show the album cover
        try
        {
            playing = new ImagePanel(controller);
            add(playing, BorderLayout.CENTER);
        } catch (IOException ioe) {
            controller.error(ioe);
        }

        // show the playlist as table
        commingSoon = new SimpleTable();
        add(commingSoon, BorderLayout.LINE_START);

        // play and pause button
        try
        {
            playButton = new PlayPanel(controller);
            add(playButton, BorderLayout.EAST);
            pauseButton();
        } catch (IOException ioe) {
            controller.error(ioe);
        }

        repaint();
        setVisible(true);
    }

    public void updatePlaylist(Playlist playlist) {
        commingSoon.setDataModel(new SimpleTableModel());
        for (int i = 0; i < playlist.size(); i++) {
            commingSoon.getDataModel().set(i, playlist.elementAt(i).getSongName());
        }
    }

    public void setReadable()
    {
        footerPanel.setReadable();
    }

    public void setWritable()
    {
        footerPanel.setWritable();
    }

    public void setClean()
    {
        footerPanel.setClean();
    }

    public void setOutput(String next)
    {
        footerPanel.setSong(next);
    }

    public void playButton()
    {
        try {
            playButton.setButton(ImageIO.read(new File(controller.getReferencePath() + File.separator + "resources" + File.separator + "play.png")));
        } catch (IOException e) {
            controller.error(e);
        }
        repaint();
    }

    public void ffButton()
    {
        try {
            playButton.setButton(ImageIO.read(new File(controller.getReferencePath() + File.separator + "resources" + File.separator + "ff.png")));
        } catch (IOException e) {
            controller.error(e);
        }
        repaint();
    }

    public void pauseButton()
    {
        try {
            playButton.setButton(ImageIO.read(new File(controller.getReferencePath() + File.separator + "resources" + File.separator + "pause.png")));
        } catch (IOException e) {
            controller.error(e);
        }
        repaint();
    }

    public void setActualSong(Song next)
    {
        footerPanel.setSong("PLAYING ::: " + next.getSongName());
        playing.setImage(next.getImage());
        repaint();
    }

    public String getInput()
    {
        return footerPanel.getSong();
    }

}
