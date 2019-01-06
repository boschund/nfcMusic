package ch.bod.nfcMusic.gui;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;

public class FileChooser
{
    public File choos()
    {
        // JFileChooser-Objekt erstellen
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle("Choose a MP3 File or a directory with MP3 Files");
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        chooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                String name = f.getName();
                try {
                    return f.isDirectory() || name.substring(name.lastIndexOf('.')).equalsIgnoreCase(".mp3");
                }
                catch(StringIndexOutOfBoundsException siobe)
                {
                    return false;
                }
            }

            @Override
            public String getDescription() {
                return "*.mp3";
            }
        });
        // Dialog zum Oeffnen von Dateien anzeigen
        int rueckgabeWert = chooser.showOpenDialog(null);

        /* Abfrage, ob auf "Öffnen" geklickt wurde */
        if(rueckgabeWert == JFileChooser.APPROVE_OPTION)
        {
            return chooser.getSelectedFile();
        }
        return null;
    }
}
