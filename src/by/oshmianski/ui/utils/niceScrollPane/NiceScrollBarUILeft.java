package by.oshmianski.ui.utils.niceScrollPane;

import javax.swing.*;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: 8-058
 * Date: 05.01.13
 * Time: 7:32
 */
public class NiceScrollBarUILeft extends BasicScrollBarUI {
    private static final Color colorWhite = new Color(0xF0F0F0);
    private static final Color bgColor = new Color(0xD4D4D4);

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
        Graphics2D g2d = (Graphics2D) g;

        int x = r.x + 1;

        int y;
        int h;
        if (UIManager.getLookAndFeel().getID().equalsIgnoreCase("nimbus")) {
            y = r.y + 9;
            h = r.height - 19;
        } else {
            y = r.y + 1;
            h = r.height - 3;
        }

        int w = 8;


        GradientPaint gpParent = new GradientPaint(
                x, y, colorWhite,
                x + w, y, bgColor,
                true);
        g2d.setPaint(gpParent);

        g2d.fillRect(x + 1, y + 1, w - 1, h - 1);

        g2d.setColor(new

                Color(0xC9C9C9)

        );
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
