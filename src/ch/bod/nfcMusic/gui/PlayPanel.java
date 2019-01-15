package ch.bod.nfcMusic.gui;

import ch.bod.nfcMusic.MusicController;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PlayPanel extends JPanel {

    BufferedImage img;
    MusicController controller;
    public enum BUTTON {PLAY, PAUSE, FF};

    public void paint(Graphics g) {
        g.drawImage(img, 5, 100, 150, 150, null);
    }

    public PlayPanel(MusicController controller) throws IOException {
        setButton(null);
        setPreferredSize(new Dimension(200, 400));
        controller = controller;
        addMouseListener(controller);
    }

    private void setImage(BufferedImage image)
    {
        img = image;
    }

    public void setButton(BufferedImage file)
    {
        setImage(file);
    }

}