package by.oshmianski.ui.utils.niceScrollPane;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: 8-058
 * Date: 08.01.13
 * Time: 10:58
 */
public class NiceLRConer extends JLabel {
    public NiceLRConer() {
        super();
        setBackground(new Color(0xF5F5F5));
        setBorder(BorderFactory.createMatteBorder(1, 1, 0, 0, new Color(0xE6E6E6)));
        setOpaque(true);
    }
}
