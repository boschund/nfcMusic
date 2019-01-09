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
        g.drawImage(img, 50, 50, 250, 250, null);
    }

    public ImagePanel() throws IOException {
        setImage(ImageIO.read(new File("resources/metal.png")));
    }

    public void setImage(BufferedImage image)
    {
        img = image;
    }

}