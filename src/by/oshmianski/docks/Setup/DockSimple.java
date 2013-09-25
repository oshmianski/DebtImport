package by.oshmianski.docks.Setup;

import bibliothek.gui.dock.common.DefaultSingleCDockable;
import by.oshmianski.main.AppletWindow;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: VintsalovichS
 * Date: 9/16/13
 * Time: 10:00 AM
 */
public class DockSimple extends DefaultSingleCDockable{
    protected JPanel panel;

    public DockSimple(String id, Icon icon, String title) {
        super(id, icon, title);

        panel = new JPanel(new BorderLayout());
        panel.setBackground(AppletWindow.REPORT_PANEL_BACKGROUND);

        add(panel);

        setCloseable(true);
        setExternalizable(false);
    }
}
