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
import by.oshmianski.objects.DataMainItem;
import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: 8-058
 * Date: 01.04.13
 * Time: 16:06
 */
public class GUI {
    private JApplet applet;
    private JPanel panelMain;
    private AppletWindowProvider window;
    private DockingContainer dockingContainer;

    private EventList<DataMainItem> dataMainItems;

    public GUI(JApplet applet, EventList<DataMainItem> dataMainItems) {
        this.applet = applet;
        this.dataMainItems = dataMainItems;
    }

    public void create() {
        panelMain = new JPanel(new BorderLayout());
        panelMain.setBackground(Color.WHITE);

        window = new AppletWindowProvider(applet);
        window.start();

        applet.getContentPane().add(panelMain);

        dockingContainer = new DockingContainer(window, panelMain, dataMainItems);

        /* The CLayoutChoiceMenuPiece creates a dynamic menu which allows us to
        * save and load the layout. In doing so we will use the EditorFactory. */
        JMenuBar menubar = new JMenuBar();
        RootMenuPiece layout = new RootMenuPiece("Окна", false);
        layout.add(new SingleCDockableListMenuPiece(dockingContainer.getControl()));
        menubar.add(layout.getMenu());
        applet.setJMenuBar(menubar);

        panelMain.add(new StatusBar(), BorderLayout.SOUTH);
    }

    public void show() {
        applet.getContentPane().add(panelMain);
        dockingContainer.setVisibleFilters(false);

        Loader loaderTI = new LoadTemplateImport(dockingContainer.getUIProcessor());
        loaderTI.execute();
    }

    public void stop() {
        CControl control = dockingContainer.getControl();

        while (control.getCDockableCount() > 0) {
            SingleCDockable dock = (SingleCDockable) control.getCDockable(0);

            control.removeDockable(dock);
        }

        control.destroy();

        dockingContainer.dispose();

        for (DataMainItem dataMainItem : dataMainItems)
            dataMainItem.getDataChildItems().clear();

        dataMainItems.clear();
        dataMainItems.dispose();
        dataMainItems = null;

        window.stop();
        window = null;
    }
}
