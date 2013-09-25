package by.oshmianski.ui.utils.niceScrollPane;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: 8-058
 * Date: 10.01.13
 * Time: 9:26
 */
public class NiceScrollPane extends JScrollPane {
    private JTable templateTable;

    public NiceScrollPane(Component view) {
        super(view);

        JScrollBar hsb = getHorizontalScrollBar();
        hsb.setUI(new NiceScrollBarUIBottom());
        hsb.setBackground(new Color(0xF5F5F5));
        hsb.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(0xE6E6E6)));
        hsb.setPreferredSize(new Dimension(Integer.MAX_VALUE, 12));

        JScrollBar vsb = getVerticalScrollBar();
        vsb.setUI(new NiceScrollBarUILeft());
        vsb.setPreferredSize(new Dimension(12, Integer.MAX_VALUE));
        vsb.setBackground(new Color(0xF5F5F5));
        vsb.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, new Color(0xE6E6E6)));

        setCorner(NiceScrollPane.LOWER_RIGHT_CORNER, new NiceLRConer());

        String data[][] = {{""}};
        String col[] = {""};
        DefaultTableModel model = new DefaultTableModel(data,col);
        templateTable = new JTable(model);

        JTableHeader corner = templateTable.getTableHeader();
        corner.setReorderingAllowed(false);
        corner.setResizingAllowed(false);
        setCorner(NiceScrollPane.UPPER_RIGHT_CORNER, corner);
    }
}
