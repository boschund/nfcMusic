package ch.bod.nfcMusic.gui;

import ch.bod.nfcMusic.MusicController;

import javax.swing.*;


public class FooterPanel extends JPanel
{

    private JTextField scan;
    private JButton search;
    private JRadioButton r_read;
    private JRadioButton r_write;
    private JRadioButton r_clean;

    public FooterPanel create(MusicController controller, MusicController.MODE mode) {
        setLayout(new BoxLayout(this, javax.swing.BoxLayout.X_AXIS));

        scan = new JTextField("WELCOME");
        add(scan);
        scan.setVisible(true);

        search = new JButton("...");
        search.setActionCommand(MusicController.SEARCH);
        search.addActionListener(controller);
        add(search);
        search.setVisible(true);

        r_read = new JRadioButton("Lesen");
        r_read.setActionCommand(MusicController.BUTTON_READ);
        r_read.addActionListener(controller);
        r_write = new JRadioButton("Schreiben");
        r_write.setActionCommand(MusicController.BUTTON_WRITE);
        r_write.addActionListener(controller);
        r_clean = new JRadioButton("Löschen");
        r_clean.setActionCommand(MusicController.BUTTON_CLEAN);
        r_clean.addActionListener(controller);

        ButtonGroup rw = new ButtonGroup();
        rw.add(r_read);
        rw.add(r_write);
        rw.add(r_clean);
        add(r_read);
        r_read.setVisible(true);
        add(r_write);
        r_write.setVisible(true);
        add(r_clean);
        r_clean.setVisible(true);

        if (mode == MusicController.MODE.WRITABEL)
        {
            setWritable();
        }
        if (mode == MusicController.MODE.READABLE)
        {
            setWritable();
        }
        if (mode == MusicController.MODE.DEV){}


        return this;
    }

    public void setReadable()
    {
        r_write.setEnabled(false);
        r_read.setEnabled(true);
        search.setEnabled(false);
        r_clean.setEnabled(false);
    }

    public void setWritable()
    {
        r_write.setEnabled(true);
        r_read.setEnabled(false);
        search.setEnabled(true);
        r_clean.setEnabled(false);
    }

    public void setClean()
    {
        r_write.setEnabled(false);
        r_read.setEnabled(false);
        r_clean.setEnabled(true);
        search.setEnabled(true);
        scan.setText("--> CLEANING CARD <--");
    }

    public void setSong(String next)
    {
        scan.setText(next);
        repaint();
    }

    public String getSong()
    {
        return scan.getText();
    }

    public boolean reading()
    {
        return r_read.isSelected();
    }

    public boolean writeing()
    {
        return r_write.isSelected();
    }

    public boolean cleaning()
    {
        return r_clean.isSelected();
    }
}
