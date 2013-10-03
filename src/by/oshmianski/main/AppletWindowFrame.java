package by.oshmianski.main;

import by.oshmianski.ui.GUIFrame;
import by.oshmianski.ui.utils.Icons;
import by.oshmianski.utils.AppletParams;
import by.oshmianski.utils.IconContainer;
import by.oshmianski.utils.MyLog;
import by.oshmianski.utils.SwingUIUtils;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: VintsalovichS
 * Date: 9/13/13
 * Time: 9:13 AM
 */
public class AppletWindowFrame extends JFrame {
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

    private GUIFrame gui;

    public AppletWindowFrame(String server) {
        SwingUIUtils.getInstance().setLAF();

        InputStream in = getClass().getClassLoader().getResourceAsStream("config.properies");
        Properties props = new Properties();
        try {
            props.load(in);
        } catch (IOException e) {
            MyLog.add2Log(e);
        }

        AppletParams ap = AppletParams.getInstance();
        ap.setServer(server);
        ap.setDbReplicaID(props.getProperty("dbReplicaID"));
        ap.setViewFieldRef(props.getProperty("viewFieldRef"));
        ap.setViewKeyRef(props.getProperty("viewKeyRef"));
        ap.setViewLinkRef(props.getProperty("viewLinkRef"));
        ap.setViewRuleRef(props.getProperty("viewRuleRef"));
        ap.setViewObjectRef(props.getProperty("viewObjectRef"));
        ap.setViewTI(props.getProperty("viewTI"));

        gui = new GUIFrame(this);
        gui.create();
        gui.show();

        setTitle("Импорт");
        setIconImage(Icons.iconToImage(IconContainer.getInstance().loadImage("import.png")));
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try {
//                    SwingUtilities.invokeAndWait(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
                    gui.stop();
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    });
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    dispose();
//                    System.exit();
                }
            }
        });

        pack();
        setVisible(true);
        setPreferredSize(new Dimension(200, 200));

        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    public static void main(String[] args) {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    new AppletWindowFrame("");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}