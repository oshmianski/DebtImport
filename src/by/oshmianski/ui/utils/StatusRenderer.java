package by.oshmianski.ui.utils;/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import by.oshmianski.objects.Status;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StatusRenderer extends DefaultTableCellRenderer implements TableCellRenderer {

    private boolean isBold;
    private int swingConstantsAlign;

    public StatusRenderer(boolean isBold, int swingConstantsAlign) {
        this.isBold = isBold;
        this.swingConstantsAlign = swingConstantsAlign;
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        JLabel label = (JLabel) c;

        if (value == Status.OK)
            label.setForeground(new Color(0x008000));

        if (value == Status.WARNING)
            label.setForeground(new Color(0xC26802));

        if (value == Status.ERROR)
            label.setForeground(new Color(0xFF0000));

        if (swingConstantsAlign != -1) {
            label.setHorizontalAlignment(swingConstantsAlign);
        }

        if (hasFocus) {
            ((JComponent) c).setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(115, 164, 209)),
                    BorderFactory.createLineBorder(new Color(192, 217, 236))));
        } else {
            ((JComponent) c).setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 2));
        }

        Font newLabelFont;
        if (isBold) {
            newLabelFont = new Font(label.getFont().getName(), Font.BOLD, 12);

            label.setFont(newLabelFont);
        } else {
            newLabelFont = new Font(label.getFont().getName(), Font.PLAIN, 11);

            label.setFont(newLabelFont);
        }

        return c;
    }
}
