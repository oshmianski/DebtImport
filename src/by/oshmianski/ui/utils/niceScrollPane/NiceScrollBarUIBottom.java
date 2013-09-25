package by.oshmianski.ui.utils.niceScrollPane;

import javax.swing.*;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

public class NiceScrollBarUIBottom extends BasicScrollBarUI {
    private static final Color colorWhite = new Color(0xF0F0F0);
    private static final Color bgColor = new Color(0xD4D4D4);


    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
        Graphics2D g2d = (Graphics2D) g;

        int y = r.y + 1;

        int x;
        int w;
        if (UIManager.getLookAndFeel().getID().equalsIgnoreCase("nimbus")) {
            x = r.x + 9;
            w = r.width - 19;
        } else {
            x = r.x + 1;
            w = r.width - 3;
        }


        int h = 8;

        GradientPaint gpParent = new GradientPaint(
                x, y, colorWhite,
                x, y + h, bgColor,
                true);
        g2d.setPaint(gpParent);

        g2d.fillRect(x + 1, y + 1, w - 1, h - 1);

        g2d.setColor(new Color(0xC9C9C9));
        g2d.drawRoundRect(x, y, w, h, 3, 3);
    }

    protected JButton createDecreaseButton(int orientation)

    {

        JButton button = new BasicArrowButton(orientation) {

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(0, 0);
            }

        };

        return button;

    }

    protected JButton createIncreaseButton(int orientation)

    {

        JButton button = new BasicArrowButton(orientation) {

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(0, 0);
            }

        };

        return button;

    }
}
