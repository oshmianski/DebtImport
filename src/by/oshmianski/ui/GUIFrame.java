package by.oshmianski.ui;

import bibliothek.gui.dock.common.CControl;
import bibliothek.gui.dock.common.SingleCDockable;
import bibliothek.gui.dock.common.menu.SingleCDockableListMenuPiece;
import bibliothek.gui.dock.facile.menu.RootMenuPiece;
import bibliothek.gui.dock.util.AppletWindowProvider;
import by.oshmianski.docks.Setup.DockingContainer;
import by.oshmianski.docks.Setup.StatusBar;
import by.oshmianski.loaders.LoadTemplateImport;
import by.oshmianski.loaders.Loader;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: 8-058
 * Date: 01.04.13
 * Time: 16:06
 */
public class GUIFrame {
    private JFrame frame;
    private JPanel panelMain;
    private DockingContainer dockingContainer;

    public GUIFrame(JFrame frame) {
        this.frame = frame;
    }

    public void create() {
        panelMain = new JPanel(new BorderLayout());
        panelMain.setBackground(Color.WHITE);

        frame.getContentPane().add(panelMain);

        dockingContainer = new DockingContainer(frame, panelMain);

        /* The CLayoutChoiceMenuPiece creates a dynamic menu which allows us to
        * save and load the layout. In doing so we will use the EditorFactory. */
        JMenuBar menubar = new JMenuBar();
        RootMenuPiece layout = new RootMenuPiece("Окна", false);
        layout.add(new SingleCDockableListMenuPiece(dockingContainer.getControl()));
        menubar.add(layout.getMenu());
        frame.setJMenuBar(menubar);

        panelMain.add(new StatusBar(), BorderLayout.SOUTH);
    }

    public void show() {
        Loader loaderTI = new LoadTemplateImport(dockingContainer.getUIProcessor());
        loaderTI.execute();

        frame.getContentPane().add(panelMain);
        dockingContainer.setVisibleFilters(false);
    }

    public void stop() {
        CControl control = dockingContainer.getControl();

        dockingContainer.dispose();

        while (control.getCDockableCount() > 0) {
            SingleCDockable dock = (SingleCDockable) control.getCDockable(0);

            control.removeDockable(dock);
        }

        control.destroy();
    }

    public DockingContainer getDockingContainer() {
        return dockingContainer;
    }
}
