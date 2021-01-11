package ch.bod.nfcMusic;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import ch.bod.nfcMusic.gui.AbstractMusicGUI;
import ch.bod.nfcMusic.gui.FileChooser;
import ch.bod.nfcMusic.gui.FullScreenMusicGUI;
import ch.bod.nfcMusic.gui.MusicGUI;
import ch.bod.nfcMusic.sky.rfid.RfidListener;
import ch.bod.nfcMusic.sound.Playlist;
import ch.bod.nfcMusic.sound.Song;
import ch.bod.nfcMusic.sound.ThreadedMp3Player;

public class MusicController implements ActionListener, MouseListener
{
    public static final String SEARCH = "search";
    public static final String BUTTON_READ = "b_read";
    public static final String BUTTON_WRITE = "b_write";
    public static final String BUTTON_CLEAN = "b_clean";

    public enum MODE {WRITABEL, READABLE, DEV, CLEAN};

    private Playlist playlist = new Playlist();
    private Song actualSong;
    //private NdefUltralightTagScanner _tagScanner;
    private RfidListener _tagScanner;

    private volatile static ThreadedMp3Player player;
    private FileChooser fileChooser = new FileChooser();
    private AbstractMusicGUI gui;
    private String referencePath;

    public static MODE mode = MODE.READABLE;
    public boolean kiosk = true;

    public static void main(String[] args)
    {
    	MODE initial = MODE.DEV;
        try {
        	init(args);
            //UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
            //UIManager.setLookAndFeel("com.jtattoo.plaf.bernstein.BernsteinLookAndFeel");
            //UIManager.setLookAndFeel("com.jtattoo.plaf.mcwin.McWinLookAndFeel");
            //*LookAndFeel
        	
        	new MusicController(initial);
        }
        catch (Throwable e) {
            System.out.println(e);
        }
    }
    
    private static MODE init(String[] args)
    {
    	MODE initial = MODE.DEV;
    	if (args == null || args.length == 0 || args[0] == null)
    	{
    		helptext();
    	}
    	else
    	{
    		if (args[0].equalsIgnoreCase("dev"))
    				initial = MODE.DEV;
    		else
    		if (args[0].equalsIgnoreCase("read"))
				initial = MODE.READABLE;
    		else
    		if (args[0].equalsIgnoreCase("write"))
				initial = MODE.WRITABEL;
    		else
			if (args[0].equalsIgnoreCase("clean"))
    				initial = MODE.CLEAN;
			else
			helptext();
    	}
    	return initial;
    }
    
    private static void helptext()
    {
    	System.out.println(".................................................");
    	System.out.println("... java ch.bod.nfcMusic.MusicController [arg]...");
    	System.out.println("...                                           ...");
    	System.out.println("... arg = dev                                 ...");
    	System.out.println("...       developermode                       ...");
    	System.out.println("...                                           ...");
    	System.out.println("... arg = read                                ...");
    	System.out.println("...       cardreader-mode                     ...");
    	System.out.println("...                                           ...");
    	System.out.println("... arg = write                               ...");
    	System.out.println("...       cardwriter-mode                     ...");
    	System.out.println("...                                           ...");
    	System.out.println("... arg = clean                               ...");
    	System.out.println("...       cardcleaner-mode                    ...");
    	System.out.println("...                                           ...");
    	System.out.println("... anything else prints this helptext        ...");
    	System.out.println(".................................................");

		System.exit(0);
    }

    public MusicController(MODE mode)
    {
    	if (kiosk)
    		gui = new FullScreenMusicGUI(this, mode);
    	else
    		gui = new MusicGUI(this, mode);
        //Initialize Cards Scanner
        _tagScanner = new RfidListener(this);
        // intro
        File ref = new File("ref");
        referencePath = ref.getAbsolutePath().substring(0, ref.getAbsolutePath().length()-4);
        player = new ThreadedMp3Player(this);
        player.start();
        gui.start();
        callNext(new Song(new File(referencePath + File.separator+ "resources" + File.separator + "intro.mp3")));
    }

    public java.lang.String getReferencePath() {
        return referencePath;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(e.getActionCommand() == SEARCH)
        {
            File selected = fileChooser.choos();
            if (selected == null)
                return;
            if (selected.isFile()) {
                Song song = new Song(selected);
                gui.setActualSong(song);
                callNext(song);
            }
            if (selected.isDirectory())
            {
                gui.setOutput(selected.getPath());
            }
        }
        if(e.getActionCommand() == BUTTON_READ)
        {
            gui.setReadable();
            mode = MODE.READABLE;
        }
        if(e.getActionCommand() == BUTTON_WRITE)
        {
            gui.setWritable();
            mode = MODE.WRITABEL;
        }
        if(e.getActionCommand() == BUTTON_CLEAN)
        {
            gui.setClean();
            mode = MODE.CLEAN;
        }
    }

    public MODE getMode()
    {
        return mode;
    }

    public Song getNext()
    {
        try {
            actualSong = playlist.remove(0);
            gui.updatePlaylist(playlist);
            gui.setActualSong(actualSong);
        }catch(ArrayIndexOutOfBoundsException fertig)
        {
            return null;
        }
        return actualSong;
    }

    public String getInput()
    {
        return gui.getInput();
    }

    public void resetPlaylist()
    {
        info("reset playlist");
        playlist.removeAllElements();
        gui.updatePlaylist(playlist);
        gui.setActualSong(actualSong);
        player.resetPlayer();
    }

    /**
     * Add song with Card ID
     * @param song
     */
    public void addToPlaylist(Song song)
    {
        info("Card detected with song : " + song.getPath());
        playlist.add(song);
        updateButton();
        gui.updatePlaylist(playlist);
    }

    /**
     * Calls next song with Card ID
     * @param song
     */
    public void callNext(Song song)
    {
        playlist.addElement(song);
        actualSong = song;
        play();
    }

    /**
     * Calls next songs with Card ID
     */
    public void callNext()
    {
        actualSong = playlist.elementAt(0);
        play();
    }

    private void pause()
    {
        player.resetPlayer();
    }

    private void play()
    {
        gui.setActualSong(actualSong);
        player.play();
    }

    private void updateButton()
    {
        if (playlist.size() < 1)
            gui.playButton();
        else
            gui.ffButton();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (player.getState().toString().equalsIgnoreCase("TIMED_WAITING")) {
            info("playing");
            updateButton();
            pause();
        }
        else {
            info("paused");
            gui.pauseButton();
            callNext(actualSong);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public void error(Throwable e_e)
    {
        System.out.println("............. e_e .............." + e_e);
        e_e.printStackTrace();
        System.out.println("..............................");
    }

    public void warning(Exception e_e)
    {
        System.out.println("............. e_e .............." + e_e);
    }

    public void info(String e_e)
    {
        System.out.println(".:. õ_õ .:." + e_e);
    }

    /***********************************************************************/
    /* the following code is only uses in case of Mifarecards
    */

    public void setRes(String res) {
        String path = "c:/temp";
        if (res != null && !res.equalsIgnoreCase(""))
            path = res.trim();
        File songFile = new File(path);
        if (songFile.exists() && songFile.isFile()) {
            //Here comes a song
            callNext(new Song(new File(path)));
        } else if (songFile.exists() && songFile.isDirectory()) {
            resetPlaylist();
            File[] listOfFiles = songFile.listFiles();
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile())
                    if (listOfFiles[i].getName().endsWith("mp3"))
                        addToPlaylist(new Song(listOfFiles[i]));
            }
            callNext();
        } else {
            info("Scanned File : " + path + " is not existent !");
        }
    }

}
