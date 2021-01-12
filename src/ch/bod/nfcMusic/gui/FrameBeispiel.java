package ch.bod.nfcMusic.gui;

import java.awt.BorderLayout;

// Import-Anweisung für unseren JFrame
import javax.swing.JFrame;
// Import-Anweisung für unser JLabel
import javax.swing.JLabel;
import javax.swing.JPanel;

public class FrameBeispiel extends JFrame
{
    public static void main(String[] args)
    {
        /* Erzeugung eines neuen Frames mit dem
           Titel "Mein JFrame Beispiel" */
        FrameBeispiel meinFrame = new FrameBeispiel();
        meinFrame.setAlwaysOnTop(true);
        meinFrame.setLayout(new BorderLayout(20, 20));
        meinFrame.start();
    }

    public void start()
    {
        /* Wir setzen die Breite und die Höhe
           unseres Fensters auf 200 Pixel */
        setSize(400,200);
        /* Hinzufügen einer einfachen Komponente
           (hier: JLabel) */
        JPanel northPanel = new JPanel();
        northPanel.setSize(50,50);
        add(northPanel, BorderLayout.NORTH);
        
        JPanel southPanel = new JPanel();
        southPanel.add(new JLabel("Beispiel JLabel1"));
        southPanel.add(new JLabel("Beispiel JLabel2"));
        southPanel.add(new JLabel("Beispiel JLabel3"));
        southPanel.setVisible(true);
        add(southPanel, BorderLayout.SOUTH);
        
        JPanel panel = new JPanel();
        panel.add(new JLabel("panelpanelpanelpanelpanel"));
        panel.setVisible(true);
        add(panel, BorderLayout.EAST);
        // Wir lassen unseren Frame anzeigen
        setVisible(true);
    }
}
