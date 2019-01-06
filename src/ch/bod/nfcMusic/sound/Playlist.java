package ch.bod.nfcMusic.sound;

import java.util.Vector;

public class Playlist extends Vector<Song> {

    private String name = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
