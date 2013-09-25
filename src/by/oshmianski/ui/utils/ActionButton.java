package by.oshmianski.ui.utils;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created with IntelliJ IDEA.
 * User: 8-058
 * Date: 02.04.13
 * Time: 14:44
 */
public class ActionButton extends JButton implements MouseListener {
    private String title;

    private static final Color bgOver;
    private static final Color bg;
    private static final Color buttonBorder;

    static {
        bgOver = new Color(0xB5BED6);
        bg = new Color(0xF0F0F0);
        buttonBorder = new Color(0xDFDFDF);
    }

    public ActionButton(String title, Icon icon, Dimension dim, String toolTipText) {
        super(title, icon);
        this.title = title;

        setUI(new BasicButtonUI());

        setBackground(bg);
        setOpaque(true);
        setBorderPainted(true);
        setPreferredSize(dim);
        setMinimumSize(dim);
        setMaximumSize(dim);
        setToolTipText(toolTipText);

        setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(1, 1, 1, 1, buttonBorder),
                        BorderFactory.createEmptyBorder(1, 1, 1, 1)
                ));

        addMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(0x95979C)),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)
                ));
        setBackground(bgOver);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(1, 1, 1, 1, buttonBorder),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)
                ));
        setBackground(bg);
    }
}
