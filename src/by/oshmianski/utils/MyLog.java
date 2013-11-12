package by.oshmianski.utils;

import by.oshmianski.docks.DockLog;

import javax.swing.*;
import java.awt.*;
import java.io.PrintWriter;
import java.io.StringWriter;

public class MyLog {
    private static DockLog dockableText;

    private MyLog() {
    }

    public static void setDock(DockLog dockableText) {
        MyLog.dockableText = dockableText;
    }

    public static void clear() {
        if (dockableText != null) {
            dockableText.setText(null);
        }
    }

    public static void add2Log(Exception e) {
        e.printStackTrace();
        try {
            StringWriter writerStr = new StringWriter();
            PrintWriter myPrinter = new PrintWriter(writerStr);

            e.printStackTrace(myPrinter);

            add2Log(writerStr.toString(), true, Color.RED);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void add2Log(String text) {
        add2Log(text, false);
    }

    public static void add2Log(String text, boolean isFocus) {
        add2Log(text, isFocus, Color.BLACK);
    }

    public static void add2Log(String text, boolean isFocus, Color color) {
        if (dockableText != null) {
            if (isFocus) {
                dockableText.setVisible(true);
                dockableText.toFront();
            }

            dockableText.appendText(text, color);
        }
    }

//    public static void add2Log(final String text, final boolean isFocus, final Color color) {
//        if (SwingUtilities.isEventDispatchThread()) {
//            add2LogEDT(text, isFocus, color);
//        } else {
//            Runnable shell = new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        add2LogEDT(text, isFocus, color);
//                    } catch (Exception ex) {
//                        throw new RuntimeException(ex);
//                    }
//                }
//            };
//            SwingUtilities.invokeLater(shell);
//        }
//
//        System.out.println(text);
//    }
}
