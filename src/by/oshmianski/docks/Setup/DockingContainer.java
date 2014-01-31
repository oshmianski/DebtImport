package by.oshmianski.docks.Setup;

import bibliothek.extension.gui.dock.theme.FlatTheme;
import bibliothek.gui.DockStation;
import bibliothek.gui.Dockable;
import bibliothek.gui.dock.DockElement;
import bibliothek.gui.dock.common.CControl;
import bibliothek.gui.dock.common.CGrid;
import bibliothek.gui.dock.event.KeyboardListener;
import bibliothek.gui.dock.focus.DefaultDockableSelection;
import bibliothek.gui.dock.themes.NoStackTheme;
import bibliothek.gui.dock.themes.ThemeManager;
import bibliothek.gui.dock.util.*;
import bibliothek.gui.dock.util.color.ColorManager;
import by.oshmianski.docks.*;
import by.oshmianski.filter.DM.FilterPanel;
import by.oshmianski.filter.CM.FilterPanelChild;
import by.oshmianski.loaders.LoadImportData;
import by.oshmianski.main.AppletWindow;
import by.oshmianski.objects.UIProcessorImpl;
import by.oshmianski.ui.edt.EDTInvocationHandler;
import by.oshmianski.ui.edt.UIProcessor;
import by.oshmianski.utils.MyLog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.lang.reflect.Proxy;

/**
 * Created with IntelliJ IDEA.
 * User: oshmianski
 * Date: 15.09.13
 * Time: 21:52
 */
public class DockingContainer {
    private CControl control;
    private DockHeader dockHeader;
    private DockSimple dockDataMainFilter;
    private DockSimple dockDataChildFilter;
    private DockDataMain dockDataMain;
    private DockDataChild dockDataChild;
    private DockObjectTree dockObjectTree;
    private DockActions dockActions;
    private DockInfo dockInfo;
    private DockAddressParser dockAddressParser;
    private DockLog dockLog;
    private JPanel mainPanel;

    private LoadImportData loader;
    private UIProcessor uiProcessor;

    public DockingContainer(JFrame frame, JPanel mainPanel) {
        control = new CControl(frame, true);
        this.mainPanel = mainPanel;

        construct();
    }
    public DockingContainer(AppletWindowProvider window, JPanel mainPanel) {
        control = new CControl(window, true);
        this.mainPanel = mainPanel;

        construct();
    }

    private void construct(){
        mainPanel.add(control.getContentArea(), BorderLayout.CENTER);

        FlatTheme dockTheme = new FlatTheme();
        dockTheme.setDockableSelection(new MyDockSelection());
        control.setTheme(new NoStackTheme(dockTheme));

//        EclipseTheme eclipseTheme = new EclipseTheme();
//        eclipseTheme.setDockableSelection(new MyDockSelection());
//        control.setTheme(new NoStackTheme(eclipseTheme));

        control.getController().getKeyboardController().addListener(new StackTabSwitcher());

        control.putProperty(CControl.KEY_CLOSE, KeyStroke.getKeyStroke(KeyEvent.VK_W, KeyEvent.CTRL_DOWN_MASK));

        ColorManager colors = control.getController().getColors();
        colors.put(Priority.CLIENT, "stack.border.edges", AppletWindow.REPORT_PANEL_BACKGROUND);
        colors.put(Priority.CLIENT, "flap.button.border.edges", AppletWindow.REPORT_PANEL_BACKGROUND);
        colors.put(Priority.CLIENT, "stack.tab.top", AppletWindow.REPORT_PANEL_BACKGROUND);

        control.getController().getThemeManager().setBackgroundPaint(ThemeManager.BACKGROUND_PAINT + ".station.flap", new MyPaint());
        control.getController().getThemeManager().setBackgroundPaint(ThemeManager.BACKGROUND_PAINT + ".station.split", new MyPaint());
        control.getController().getThemeManager().setBackgroundPaint(ThemeManager.BACKGROUND_PAINT + ".tabPane", new MyPaint());

        CGrid grid = new CGrid(control);

        dockDataMainFilter = new DockDataMainFilter();
        dockDataChildFilter = new DockDataChildFilter();
        dockHeader = new DockHeader(this);
        dockInfo = new DockInfo();
        dockDataMain = new DockDataMain(this);
        dockDataChild = new DockDataChild(this, null);
        dockActions = new DockActions(this);
        dockLog = new DockLog();
        dockObjectTree = new DockObjectTree(this, null);
        dockAddressParser = new DockAddressParser(this);
        MyLog.setDock(dockLog);

        control.addDockable(dockLog);

        uiProcessor = (UIProcessor) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{UIProcessor.class},
                new EDTInvocationHandler(new UIProcessorImpl(dockHeader, dockActions, dockInfo, dockDataMain, dockDataChild, dockObjectTree, dockAddressParser)));

        loader = new LoadImportData(uiProcessor, true);

        control.addDockable(dockDataMainFilter);
        control.addDockable(dockDataChildFilter);
        control.addDockable(dockAddressParser);

        grid.add(0, 0, 80, 20, dockHeader);
        grid.add(0, 20, 60, 40, dockDataMain);
        grid.add(60, 20, 20, 40, dockDataMainFilter);
        grid.add(0, 60, 30, 40, dockDataChild);
        grid.add(30, 60, 30, 40, dockObjectTree);
        grid.add(60, 60, 20, 40, dockDataChildFilter);
        grid.add(80, 0, 20, 50, dockInfo);
        grid.add(80, 50, 20, 50, dockActions);

        control.getContentArea().deploy(grid);
    }

    public void setVisibleFilters(Boolean visible) {
        dockDataChildFilter.setVisible(visible);
    }

    private static class MyPaint implements BackgroundPaint {
        @Override
        public void install(BackgroundComponent backgroundComponent) {
        }

        @Override
        public void uninstall(BackgroundComponent backgroundComponent) {
        }

        public void paint(BackgroundComponent background, PaintableComponent paintable, Graphics g) {
            paintable.paintBackground(null);
            g.setColor(AppletWindow.REPORT_PANEL_BACKGROUND);
            Component component = paintable.getComponent();
            g.fillRect(0, 0, component.getWidth(), component.getHeight());
        }
    }

    private static final class StackTabSwitcher implements KeyboardListener {
        @Override
        public DockElement getTreeLocation() {
            return null;
        }

        @Override
        public boolean keyPressed(DockElement element, KeyEvent event) {
            if (event.getKeyCode() == KeyEvent.VK_RIGHT && event.isControlDown()) {
                return shift(element, 1);
            }

            return event.getKeyCode() == KeyEvent.VK_LEFT && event.isControlDown() && shift(element, -1);
        }

        @Override
        public boolean keyReleased(DockElement element, KeyEvent event) {
            return false;
        }

        @Override
        public boolean keyTyped(DockElement element, KeyEvent event) {
            return false;
        }

        private boolean shift(DockElement element, int delta) {
            // 'element' is the DockElement that currently has the focus. After the
            // tab changed this could be the DockStation (the parent) itself.

            DockStation parent = element.asDockStation();
            if (parent == null) {
                parent = element.asDockable().getDockParent();
            }

            // we can make the method more general by not checking explicitely whether
            // the parent is a StackDockStation or not
            // if( parent instanceof StackDockStation ){
            if (parent != null) {
                Dockable focused = parent.getFrontDockable();

                int index = -1;
                for (int i = 0, n = parent.getDockableCount(); i < n; i++) {
                    if (parent.getDockable(i) == focused) {
                        index = i;
                        break;
                    }
                }
                if (index != -1) {
                    index += delta;
                    index %= parent.getDockableCount();
                    if (index < 0) {
                        index += parent.getDockableCount();
                    }
                    Dockable next = parent.getDockable(index);
                    parent.getController().setFocusedDockable(next, true);
                    return true;
                }
            }
            return false;
        }
    }

    private static class MyDockSelection extends DefaultDockableSelection {
        public MyDockSelection() {
//            setBorder(BorderFactory.createLineBorder(new Color(0x8A8A8A)));
            setBorder(BorderFactory.createLineBorder(new Color(0x808080)));

            removeAll();

            add(getList());

            revalidate();
        }
    }

    public CControl getControl() {
        return control;
    }

    public UIProcessor getUIProcessor() {
        return uiProcessor;
    }

    public void setDockDataMainFilter(FilterPanel filter){
        dockDataMainFilter.panel.add(filter.getComponent());
    }

    public void setDockDataChildFilter(FilterPanelChild filter){
        dockDataChildFilter.panel.add(filter.getComponent());
    }

    public DockHeader getDockHeader() {
        return dockHeader;
    }

    public void dispose(){
        System.out.println("DockingContainer clear...");
        dockDataMain.dispose();
        dockDataChild.dispose();
        dockAddressParser.dispose();
        dockHeader.dispose();
        System.out.println("DockingContainer clear...OK");
    }

    public LoadImportData getLoader() {
        return loader;
    }
}
