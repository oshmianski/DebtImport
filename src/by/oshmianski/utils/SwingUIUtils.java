package by.oshmianski.utils;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: 8-058
 * Date: 01.04.13
 * Time: 15:52
 */
public class SwingUIUtils {
    private static SwingUIUtils ourInstance = new SwingUIUtils();

    public static SwingUIUtils getInstance() {
        return ourInstance;
    }

    private SwingUIUtils() {
    }

    public void setLAF() {
        try {
            try {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
//                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
//                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ex) {
                try {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                } catch (Exception ee) {
                    try {
                        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                    } catch (Exception e) {
                        MyLog.add2Log(e);
                    }
                }
            }

//            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            MyLog.add2Log(e);
        }
    }
}
