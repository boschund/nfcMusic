package ch.bod.nfcMusic.gui;

import ch.bod.nfcMusic.MusicController;

import javax.swing.*;


public class FooterPanel extends JPanel
{

    private JTextField scan;
    private JButton search;
    private JRadioButton r_read;
    private JRadioButton r_write;

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

        ButtonGroup rw = new ButtonGroup();
        rw.add(r_read);
        rw.add(r_write);
        add(r_read);
        r_read.setVisible(true);
        add(r_write);
        r_write.setVisible(true);

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
    }

    public void setWritable()
    {
        r_write.setEnabled(true);
        r_read.setEnabled(false);
        search.setEnabled(true);
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
}
