package by.oshmianski.docks;

import bibliothek.gui.dock.common.event.CFocusListener;
import bibliothek.gui.dock.common.intern.CDockable;
import by.oshmianski.docks.Setup.DockSimple;
import by.oshmianski.main.AppletWindow;
import by.oshmianski.ui.utils.niceScrollPane.NiceScrollPane;
import by.oshmianski.utils.IconContainer;
import by.oshmianski.utils.MyLog;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DockLog extends DockSimple {
    private JTextPane area;
    private StyledDocument doc;
    private Style style;

    public DockLog() {
        super("DockLog", IconContainer.getInstance().loadImage("exclamation.png"), "Сообщения");

        addFocusListener(new CFocusListener() {
            @Override
            public void focusGained(CDockable cDockable) {
                getFucus();
            }

            @Override
            public void focusLost(CDockable cDockable) {
            }
        });

        area = new JTextPane();
        doc = area.getStyledDocument();
        style = area.addStyle("I'm a Style", null);
        area.setEditable(false);

        JMenuItem item1 = new JMenuItem("Копировать");
//        item1.setIcon(ic.getImage("page_copy.png"));
        item1.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                final Action copyText = area.getActionMap().get(DefaultEditorKit.copyAction);
                copyText.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, DefaultEditorKit.copyAction));
            }
        });

        JMenuItem item2 = new JMenuItem("Очистить");
//        item2.setIcon(ic.getImage("sport_shuttlecock.png"));
        item2.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setText("");
            }
        });

        JMenuItem item3 = new JMenuItem("Выделить все");
//        item3.setIcon(ic.getImage("selection.png"));
        item3.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                final Action copyText = area.getActionMap().get(DefaultEditorKit.selectAllAction);
                copyText.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, DefaultEditorKit.selectAllAction));
            }
        });

        JPopupMenu menu = new JPopupMenu();
        menu.add(item1);
        menu.add(item2);
        menu.add(new JSeparator());
        menu.add(item3);
//        panel.add(menu);

        area.addMouseListener(
                new PopupTriggerMouseListener(
                        menu,
                        area));

        JScrollPane sp = new NiceScrollPane(area);

        sp.setBackground(AppletWindow.REPORT_PANEL_BACKGROUND);
        sp.getViewport().setBackground(AppletWindow.REPORT_PANEL_BACKGROUND);

        panel.add(sp);
    }

    public void setText(String text) {
        appendText(text, Color.BLACK, true);
    }

    public void appendText(String text) {
        appendText(text, Color.BLACK);
    }

    public void appendText(String text, Color color) {
        appendText(text, color, false);
    }

    public void appendText(final String text, final Color color, final boolean isClear) {

        if (SwingUtilities.isEventDispatchThread()) {
            appendTextInner(text, color, isClear);
        } else {
            Runnable shell = new Runnable() {
                @Override
                public void run() {
                    try {
                        appendTextInner(text, color, isClear);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
            };

            SwingUtilities.invokeLater(shell);
        }
    }

    private void appendTextInner(String text, Color color, boolean isClear) {
        StyleConstants.setForeground(style, color);

        if (isClear) {
            area.setText("");
        }

        try {
            final int docLength = doc.getLength();
            doc.insertString(docLength, text + "\n", style);
        } catch (BadLocationException e) {
            MyLog.add2Log(e);
        }
    }

    public static class PopupTriggerMouseListener extends MouseAdapter {

        private JPopupMenu popup;
        private JComponent component;

        public PopupTriggerMouseListener(JPopupMenu popup, JComponent component) {
            this.popup = popup;
            this.component = component;
        }

        private void showMenuIfPopupTrigger(MouseEvent e) {
            if (e.isPopupTrigger()) {
                popup.show(component, e.getX() + 3, e.getY() + 3);
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            showMenuIfPopupTrigger(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            showMenuIfPopupTrigger(e);
        }
    }

    public void getFucus() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                area.requestFocus();
            }
        });
    }
}
