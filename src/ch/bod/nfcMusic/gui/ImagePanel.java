package ch.bod.nfcMusic.gui;

import ch.bod.nfcMusic.MusicController;

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

    public ImagePanel(MusicController controller) throws IOException {
        setImage(ImageIO.read(new File(controller.getReferencePath() + File.separator + "resources" + File.separator + "metal.png")));
    }

    public void setImage(BufferedImage image)
    {
        img = image;
    }

}