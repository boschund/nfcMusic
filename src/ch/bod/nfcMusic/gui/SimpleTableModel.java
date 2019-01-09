package ch.bod.nfcMusic.gui;

import javax.swing.table.DefaultTableModel;

class SimpleTableModel extends DefaultTableModel {

    private int rows = 50;

    public SimpleTableModel() {
        super();
        initModelData();
    }

    public void set(int row, String value)
    {
        this.setValueAt(value, row, 0);
    }

    public void setInitAnzahlRows(int rows)
    {
        rows = rows;
        initModelData();
    }

    private void initModelData() {
    this.addColumn("Songname");
        for (int j = 0; j < rows; j++) {
            this.addRow(new Object[]{""});
        }
    }
}