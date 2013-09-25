package by.oshmianski.ui.panels;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: 8-058
 * Date: 11.04.13
 * Time: 10:54
 */
public class BasicPanel extends JPanel {
    public BasicPanel(BorderLayout borderLayout) {
        setLayout(borderLayout);
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
    }
}
