package by.oshmianski.docks;

import by.oshmianski.docks.Setup.DockSimple;
import by.oshmianski.docks.Setup.DockingContainer;
import by.oshmianski.filter.DM.FilterPanel;
import by.oshmianski.loaders.LoadImportData;
import by.oshmianski.main.AppletWindow;
import by.oshmianski.models.DataMainModel;
import by.oshmianski.objects.DataMainItem;
import by.oshmianski.ui.utils.BetterJTable2;
import by.oshmianski.ui.utils.ColorRenderer;
import by.oshmianski.ui.utils.niceScrollPane.NiceScrollPane;
import by.oshmianski.utils.IconContainer;
import by.oshmianski.utils.MyLog;
import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.swing.DefaultEventSelectionModel;
import ca.odell.glazedlists.swing.DefaultEventTableModel;
import ca.odell.glazedlists.swing.GlazedListsSwing;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;

/**
 * Created with IntelliJ IDEA.
 * User: oshmianski
 * Date: 15.09.13
 * Time: 22:14
 */
public class DockDataMain extends DockSimple {
    private DockingContainer dockingContainer;
    private EventList<DataMainItem> dataMainItems = new BasicEventList<DataMainItem>();

    private DefaultEventTableModel model;
    private EventList<DataMainItem> entries;
    private SortedList<DataMainItem> sortedEntries;
    private FilterList<DataMainItem> filteredEntries;
    private JTable table;
    private DefaultEventSelectionModel issuesSelectionModel;
    private FilterPanel filterPanel;
    private static final String frameTitle = "Данные";
    private JPopupMenu ppmenu;
    private LoadImportData loader;

    public DockDataMain(DockingContainer dockingContainer) {
        super("DockDataMain", IconContainer.getInstance().loadImage("grid.png"), frameTitle);

        this.dockingContainer = dockingContainer;

        dataMainItems.getReadWriteLock().writeLock().lock();

        table = null;
        JScrollPane sp;

        try {
            entries = GlazedListsSwing.swingThreadProxyList(this.dataMainItems);

            filterPanel = new FilterPanel(entries, true, dockingContainer);

            sortedEntries = new SortedList<DataMainItem>(entries, null);

            filteredEntries = new FilterList<DataMainItem>(sortedEntries, filterPanel.getMatcherEditor());

            model = new DefaultEventTableModel(filteredEntries, new DataMainModel());

            filterPanel.install(model);

            table = new BetterJTable2(null, true);

            ppmenu = createPopupMenu();
            table.addMouseListener(new MouseAdapter() {
                public void mouseReleased(MouseEvent Me) {
                    if (Me.isPopupTrigger()) {
                        int row = table.rowAtPoint(Me.getPoint());
                        int col = table.columnAtPoint(Me.getPoint());

                        if (!table.isRowSelected(row))
                            table.changeSelection(row, col, false, false);

                        ppmenu.show(Me.getComponent(), Me.getX(), Me.getY());
                    }
                }
            });

            table.setModel(model);

            table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            table.getColumnModel().getColumn(0).setPreferredWidth(80);
            table.getColumnModel().getColumn(1).setPreferredWidth(150);
            table.getColumnModel().getColumn(2).setPreferredWidth(500);

            DecimalFormat df = new DecimalFormat("###,##0");
            table.getColumnModel().getColumn(0).setCellRenderer(new ColorRenderer(Color.BLUE, false, df));
            table.getColumnModel().getColumn(1).setCellRenderer(new ColorRenderer(new Color(0xC26802), false));
            table.getColumnModel().getColumn(2).setCellRenderer(new ColorRenderer(Color.BLACK, false));

            table.setRowHeight(20);
            table.setShowHorizontalLines(true);
            table.setShowVerticalLines(true);
            table.setIntercellSpacing(new Dimension(1, 1));
            table.setGridColor(AppletWindow.DATA_TABLE_GRID_COLOR);
            table.setSelectionBackground(new Color(217, 235, 245));
            table.setSelectionForeground(Color.BLACK);

            table.addKeyListener(new KeyEventListener(issuesSelectionModel));

//            TableComparatorChooser.install(table, sortedEntries, AbstractTableComparatorChooser.MULTIPLE_COLUMN_KEYBOARD);

            issuesSelectionModel = new DefaultEventSelectionModel(filteredEntries);
            issuesSelectionModel.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            issuesSelectionModel.addListSelectionListener(new IssuesSelectionListener(dockingContainer, issuesSelectionModel));
            table.setSelectionModel(issuesSelectionModel);

            sp = new NiceScrollPane(table);

            sp.setBackground(AppletWindow.REPORT_PANEL_BACKGROUND);
            sp.getViewport().setBackground(AppletWindow.REPORT_PANEL_BACKGROUND);

            panel.add(sp);
            dockingContainer.setDockDataMainFilter(filterPanel);

        } catch (Exception e) {
            MyLog.add2Log(e);
        } finally {
            this.dataMainItems.getReadWriteLock().writeLock().unlock();
        }
    }

    public void clearDataMain() {
        dataMainItems.clear();
        filterPanel.getMatcherEditorDMStatuses().fireMatchAllA();
        filterPanel.getTextFilterComponent().fireMatchAllA();
    }

    public void appendDataMain(DataMainItem dataMainItem) {
        dataMainItems.add(dataMainItem);
    }

    /**
     * Listens for changes in the selection on the issues table.
     */
    private static class IssuesSelectionListener implements ListSelectionListener {
        private DefaultEventSelectionModel issuesSelectionModel;
        private DockingContainer dockingContainer;

        IssuesSelectionListener(DockingContainer dockingContainer, DefaultEventSelectionModel issuesSelectionModel) {
            super();

            this.dockingContainer = dockingContainer;
            this.issuesSelectionModel = issuesSelectionModel;
        }

        public void valueChanged(ListSelectionEvent e) {
            DataMainItem selectedItem = null;
            if (issuesSelectionModel.getSelected().size() > 0) {
                Object selectedObject = issuesSelectionModel.getSelected().get(0);
                if (selectedObject instanceof DataMainItem) {
                    selectedItem = (DataMainItem) selectedObject;
                }
            }

            if (selectedItem == null) return;

            dockingContainer.getUIProcessor().setDockDataChildItems(selectedItem);
            dockingContainer.getUIProcessor().setDockObjectTreeObjects(selectedItem);
            dockingContainer.getUIProcessor().setDockAddressParserItems(selectedItem);
        }
    }

    public EventList<DataMainItem> getDataMainItems() {
        return dataMainItems;
    }

    public void dispose() {
        System.out.println("DockDataMain clear...");

        for (DataMainItem dataMainItem : dataMainItems) {
            dataMainItem.getDataChildItems().clear();
            dataMainItem.getObjects().clear();
        }

        dataMainItems.clear();
        dataMainItems.dispose();
        dataMainItems = null;

        if (model != null) model = null;
        if (filteredEntries != null) filteredEntries.dispose();
        if (sortedEntries != null) sortedEntries.dispose();
        if (entries != null) entries.dispose();
        if (dataMainItems != null) dataMainItems.dispose();

        filterPanel.dispose();
        System.out.println("DockDataMain clear...OK");
    }

    public void setFilteredCount() {
        setTitleText(frameTitle + " [" + filteredEntries.size() + "]");
    }

    private JPopupMenu createPopupMenu() {
        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem menuAdd = new JMenuItem("Перчитать строку");
        menuAdd.setIcon(IconContainer.getInstance().loadImage("actions.png"));
        menuAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reloadLine();
            }
        });
        popupMenu.add(menuAdd);

//        JMenuItem menuRefreshModel = new JMenuItem("Обновить");
////        menuAdd.setIcon(IconContainer.getInstance().loadImage("actions.png"));
//        menuRefreshModel.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                loader = dockingContainer.getLoader();
//
//                if (loader.isExecuted()) {
//                    JOptionPane.showMessageDialog(
//                            null,
//                            "В данные момент выполняется загрузка!\n" +
//                                    "Действие отменено.",
//                            "Внимание",
//                            JOptionPane.ERROR_MESSAGE);
//
//                    return;
//                }
//
//                model.fireTableDataChanged();
//            }
//        });
//        popupMenu.add(menuRefreshModel);

        return popupMenu;
    }

    private class KeyEventListener implements KeyListener {
        private DefaultEventSelectionModel issuesSelectionModel;

        public KeyEventListener(DefaultEventSelectionModel issuesSelectionModel) {
            this.issuesSelectionModel = issuesSelectionModel;
        }

        @Override
        public void keyTyped(KeyEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void keyPressed(KeyEvent e) {
//            System.out.println(e.getKeyCode());

            if (e.getKeyCode() == KeyEvent.VK_F5) {
                reloadLine();

                e.consume();
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }

    private void reloadLine(){
        DataMainItem selectedItem = null;

        loader = dockingContainer.getLoader();
        loader.setTest(true);

        if (loader.isExecuted()) {
            JOptionPane.showMessageDialog(
                    null,
                    "В данные момент выполняется загрузка!\n" +
                            "Действие отменено.",
                    "Внимание",
                    JOptionPane.ERROR_MESSAGE);

            return;
        }

        if (issuesSelectionModel.getSelected().size() > 0) {
            Object selectedObject = issuesSelectionModel.getSelected().get(0);
            if (selectedObject instanceof DataMainItem) {
                selectedItem = (DataMainItem) selectedObject;
            }
        }

        if (selectedItem == null) return;

        selectedItem.clearData();

        DataMainItem dataMainItem = loader.getImporter().reloadItem((int) selectedItem.getLineNum());

        selectedItem.setAddressParser(dataMainItem.getAddressParser());
        selectedItem.setDataChildItems(dataMainItem.getDataChildItems());
        selectedItem.setObjects(dataMainItem.getObjects());
        if (selectedItem.getAddressParser().getAddress().isProcessedFull() && selectedItem.getAddressParser().getAddress().isProcessedFullNotService()) {
            selectedItem.setFlag2color(2);
        } else {
            selectedItem.setFlag2color(1);
        }

        dockingContainer.getUIProcessor().setDockDataChildItems(selectedItem);
        dockingContainer.getUIProcessor().setDockObjectTreeObjects(selectedItem);
        dockingContainer.getUIProcessor().setDockAddressParserItems(selectedItem);

        table.repaint(table.getVisibleRect());
    }
}
