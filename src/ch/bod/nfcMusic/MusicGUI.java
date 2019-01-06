package ch.bod.nfcMusic;

import ch.bod.nfcMusic.gui.FooterPanel;
import ch.bod.nfcMusic.gui.ImagePanel;
import ch.bod.nfcMusic.sound.Playlist;
import javafx.application.Platform;


import javax.swing.*;
import java.awt.*;
import java.io.IOException;


public class MusicGUI extends JFrame
{
    private JTextArea commingSoon;
    private ImagePanel plaing;
    private final JProgressBar loader = new JProgressBar();
    private MusicController controller;
    private FooterPanel footerPanel = new FooterPanel();

    private MusicController.MODE mode;

    MusicGUI(MusicController controller, MusicController.MODE mode)
    {
        this.controller = controller;
        this.mode = mode;
    }

    public void start() {
        try {
            // setting the frame
            setSize(500, 350);
            setTitle("NFCMusic");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);
            setIconImage(Toolkit.getDefaultToolkit().getImage("resources/metal.png"));

            //creating GUI
            createGUI();

        } catch (Exception e){
            controller.error(e);
            start();
        }
    }

    private void createGUI() {
        setLayout(new BorderLayout());

        // progressLoader
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
        try
        {
            plaing = new ImagePanel();
            add(plaing, BorderLayout.CENTER);
        } catch (IOException ò_ó) {
            controller.error(ò_ó);
        }

        commingSoon = new JTextArea("...", 5, 15);
        commingSoon.setEditable(false);
        add(commingSoon, BorderLayout.LINE_START);

        repaint();
        setVisible(true);
    }

    public void updatePlaylist(Playlist playlist) {
        commingSoon.setText("");
        for (Song song:playlist)
        {
            commingSoon.append(song.getSongName() + "\n");
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

    public void setActualSong(String next)
    {
        footerPanel.setSong(next);
    }

    public String getInput()
    {
        return footerPanel.getSong();
    }

}
