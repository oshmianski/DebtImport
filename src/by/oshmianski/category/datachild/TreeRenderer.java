package by.oshmianski.category.datachild;/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import by.oshmianski.objects.DataChildItem;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class TreeRenderer extends DefaultTableCellRenderer implements TableCellRenderer {
    private final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
    private final DecimalFormat formatterNumber = new DecimalFormat("###,###");

    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        JLabel label = (JLabel) c;

        if (value != null) {
//            if (value instanceof Date) {
//                label.setText(formatter.format(value));
//            }
//            if (value instanceof Number) {
//                label.setText(formatterNumber.format((Number) value));
//                label.setHorizontalAlignment(SwingConstants.RIGHT);
//            }

            final Font newLabelFontBold = new Font(label.getFont().getName(), Font.BOLD, 12);
            final Font newLabelFontPlain = new Font(label.getFont().getName(), Font.PLAIN, 12);

            if (value instanceof DataChildItem) {
                final DataChildItem answer = (DataChildItem) value;

//                if (item.getSortLevel() < 0) {
//                    System.out.println(item.getSortLevel() + " < 0");
//                }

//                label.setText(answer.isSynthetic() ? answer.getMainCat() : Main.qtList.get(answer.getUNIDRespQT().hashCode()).getTitle()
//                        .replace("%key1%", answer.getAnketa().getKey1())
//                        .replace("%key2%", answer.getAnketa().getKey2())
//                        .replace("%key3%", answer.getAnketa().getKey3())
//                        .replace("%key4%", answer.getAnketa().getKey4()));
                label.setText(answer.isSynthetic() ? answer.getMainCat() : answer.getValue());
                switch (answer.getIndentLevel()) {
                    case 1:
                        label.setForeground(new Color(0, 128, 128));
                        label.setFont(newLabelFontBold);
                        break;
                    case 2:
                        label.setForeground(new Color(0, 0, 128));
                        label.setFont(newLabelFontBold);
                        break;
                    case 3:
                        label.setForeground(new Color(151, 0, 0));
                        label.setFont(newLabelFontBold);
                        break;
                    case 4:
                        label.setForeground(new Color(149, 128, 0));
                        label.setFont(newLabelFontBold);
                        break;
                    case 5:
                        label.setForeground(new Color(135, 89, 67));
                        label.setFont(newLabelFontBold);
                        break;
                    case 6:
                        label.setForeground(new Color(100, 100, 100));
                        label.setFont(newLabelFontBold);
                        break;
                    case 7:
                        label.setForeground(new Color(149, 128, 110));
                        label.setFont(newLabelFontBold);
                        break;
                    case 8:
//                            label.setForeground(new Color(135, 89, 67));
                        label.setForeground(Color.BLACK);
                        label.setFont(newLabelFontBold);
                        break;
                    case -1:
                        label.setForeground(Color.BLACK);
                        label.setFont(newLabelFontPlain);
                        break;
                    default:
                        label.setForeground(Color.BLACK);
                        label.setFont(newLabelFontBold);
                        break;
                }
//                }
            }
        }
        return c;
    }
}
