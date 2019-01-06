package ch.bod.nfcMusic.sound;

import ch.bod.nfcMusic.Song;

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
