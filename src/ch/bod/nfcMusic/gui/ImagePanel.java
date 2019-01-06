package ch.bod.nfcMusic.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImagePanel extends JPanel {

    BufferedImage img;

    public void paint(Graphics g) {
        g.drawImage(img, 50, 10, 250, 250, null);
    }

    public ImagePanel() throws IOException {
        setImage("resources/metal.png");

    }

    public void setImage(String path) throws IOException
    {
        img = ImageIO.read(new File(path));
        setVisible(true);
    }

}