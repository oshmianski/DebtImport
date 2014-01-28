package by.oshmianski.ui.utils;/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class NotBooleanRenderer extends DefaultTableCellRenderer implements TableCellRenderer {

    private Color color;
    private boolean isBold;
    private final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
    private DecimalFormat formatterNumber;
    private int swingConstantsAlign;

    public NotBooleanRenderer(Color c, boolean isBold) {
        this(c, isBold, new DecimalFormat("###,##0"), -1);
    }

    public NotBooleanRenderer(Color c, boolean isBold, int swingConstantsAlign) {
        this(c, isBold, new DecimalFormat("###,##0"), swingConstantsAlign);
    }

    public NotBooleanRenderer(Color c, boolean isBold, DecimalFormat formatterNumber) {
        this(c, isBold, formatterNumber, -1);
    }

    public NotBooleanRenderer(Color c, boolean isBold, DecimalFormat formatterNumber, int swingConstantsAlign) {
        this.color = c;
        this.isBold = isBold;
        this.formatterNumber = formatterNumber;
        this.swingConstantsAlign = swingConstantsAlign;
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        JLabel label = (JLabel) c;

//        if (!isSelected) {
        label.setForeground(color);
//        }

        if (value != null) {
            if (Boolean.valueOf(value.toString())) {
                label.setForeground(Color.RED);
            }
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
