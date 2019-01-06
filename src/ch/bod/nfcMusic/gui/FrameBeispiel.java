package ch.bod.nfcMusic.gui;

// Import-Anweisung für unseren JFrame
import javax.swing.JFrame;
// Import-Anweisung für unser JLabel
import javax.swing.JLabel;

public class FrameBeispiel extends JFrame
{
    public static void main(String[] args)
    {
        /* Erzeugung eines neuen Frames mit dem
           Titel "Mein JFrame Beispiel" */
        FrameBeispiel meinFrame = new FrameBeispiel();
        meinFrame.start();
    }

    public void start()
    {
        /* Wir setzen die Breite und die Höhe
           unseres Fensters auf 200 Pixel */
        setSize(200,200);
        /* Hinzufügen einer einfachen Komponente
           (hier: JLabel) */
        add(new JLabel("Beispiel JLabel"));
        // Wir lassen unseren Frame anzeigen
        setVisible(true);
    }
}
