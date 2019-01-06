package ch.bod.nfcMusic;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Song {

    private String path = "";
    private String songName = "";
    private String interpret = "";
    private Image image;

    private File song;

    public Song(File file)
    {
            song = file;
            songName = song.getName().substring(0, song.getName().lastIndexOf("."));
            path = song.getPath();
            String parent = song.getParent();
            interpret = parent.substring(parent.lastIndexOf(File.separator) + 1, parent.length() - 1);
            try {
                image = ImageIO.read(new File(parent + File.separator + songName + ".png"));
            } catch (IOException e) {
                try {
                    image = ImageIO.read(new File("resources/metal.png"));
                } catch (IOException e1) {
                    System.out.println("::: --- connot load image --- :::");
                }
            }
    }

    public File getSong() {
        return song;
    }

    public String getPath() {
        return path;
    }

    public File getFile() {
        return song;
    }

    public String getSongName() {
        return songName;
    }

    public String getInterpret() {
        return interpret;
    }

    public Image getImage() {
        return image;
    }

}
