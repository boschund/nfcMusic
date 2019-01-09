package ch.bod.nfcMusic.gui;

import javax.swing.*;

import javax.swing.plaf.BorderUIResource;
import java.awt.*;

public class SimpleTable extends JPanel {

    private JTable table;

    public SimpleTable() {
        table = new JTable(new SimpleTableModel());
        JScrollPane pane = new JScrollPane(table);
        pane.setPreferredSize(new Dimension(150, 310));
        add(pane);
        UIManager.put("Table.focusCellHighlightBorder",
                new BorderUIResource(BorderFactory.createLineBorder(Color.red)));
        setVisible(true);
    }

    public void setDataModel(SimpleTableModel model)
    {
        model = model;
        table.setModel(model);
    }

    public SimpleTableModel getDataModel()
    {
        return (SimpleTableModel)table.getModel();
    }

}

