package by.oshmianski.main;

import by.oshmianski.ui.GUI;
import by.oshmianski.ui.utils.Icons;
import by.oshmianski.utils.AppletParams;
import by.oshmianski.utils.SwingUIUtils;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: VintsalovichS
 * Date: 9/13/13
 * Time: 9:13 AM
 */
public class AppletWindow extends JApplet {
    public static JApplet applet;

    public static Color REPORT_PANEL_BACKGROUND = new Color(252, 252, 252);
    public static Color DATA_TABLE_GRID_COLOR = new Color(0xEFEFEF);

    public static final Border EMPTY_ONE_PIXEL_BORDER = BorderFactory.createEmptyBorder(1, 1, 1, 1);

    public static final Color GLAZED_LISTS_DARK_BROWN = new Color(36, 23, 10);
    public static final Color GLAZED_LISTS_MEDIUM_BROWN = new Color(69, 64, 56);
    public static final Color GLAZED_LISTS_MEDIUM_LIGHT_BROWN = new Color(150, 140, 130);
    public static final Color GLAZED_LISTS_LIGHT_BROWN = new Color(228, 235, 242);

    public static final Icon X_ICON = Icons.x(10, 5, GLAZED_LISTS_MEDIUM_LIGHT_BROWN);
    public static final Icon PLUS_ICON = Icons.plus(10, 3, GLAZED_LISTS_MEDIUM_LIGHT_BROWN);
    public static final Icon MINUS_ICON = Icons.minus(10, 3, GLAZED_LISTS_MEDIUM_LIGHT_BROWN);

    private GUI gui;

    @Override
    public void stop() {
        try {
            EventQueue.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    try {
                        gui.stop();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init() {
        applet = this;

        SwingUIUtils.getInstance().setLAF();
        AppletParams.getInstance().getParams(this);
    }

    @Override
    public void start() {
        gui = new GUI(applet);
        gui.create();
        gui.show();
    }
}