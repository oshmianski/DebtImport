package by.oshmianski.docks;

import by.oshmianski.docks.Setup.DockSimple;
import by.oshmianski.docks.Setup.DockingContainer;
import by.oshmianski.filter.DM.FilterPanel;
import by.oshmianski.main.AppletWindow;
import by.oshmianski.models.DataMainModel;
import by.oshmianski.objects.DataMainItem;
import by.oshmianski.ui.utils.BetterJTable;
import by.oshmianski.ui.utils.ColorRenderer;
import by.oshmianski.ui.utils.StatusRenderer;
import by.oshmianski.ui.utils.niceScrollPane.NiceScrollPane;
import by.oshmianski.utils.IconContainer;
import by.oshmianski.utils.MyLog;
import ca.odell.glazedlists.*;
import ca.odell.glazedlists.gui.AbstractTableComparatorChooser;
import ca.odell.glazedlists.swing.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
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

    public DockDataMain(DockingContainer dockingContainer) {
        super("DockDataMain", IconContainer.getInstance().loadImage("grid.png"), "Данные");

        this.dockingContainer = dockingContainer;

        dataMainItems.getReadWriteLock().writeLock().lock();

        table = null;
        JScrollPane sp;

        try {
            entries = GlazedListsSwing.swingThreadProxyList(this.dataMainItems);

            filterPanel = new FilterPanel(entries, true);

            sortedEntries = new SortedList<DataMainItem>(entries, null);

            filteredEntries = new FilterList<DataMainItem>(sortedEntries, filterPanel.getMatcherEditor());

            model = new DefaultEventTableModel(filteredEntries, new DataMainModel());

            filterPanel.install(model);

            table = new BetterJTable(null);

            table.setModel(model);

            table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            table.getColumnModel().getColumn(0).setPreferredWidth(80);
            table.getColumnModel().getColumn(1).setPreferredWidth(150);
            table.getColumnModel().getColumn(2).setPreferredWidth(500);

            DecimalFormat df = new DecimalFormat("###,##0");
            table.getColumnModel().getColumn(0).setCellRenderer(new ColorRenderer(Color.BLUE, false, df));
            table.getColumnModel().getColumn(1).setCellRenderer(new StatusRenderer(false, -1));
            table.getColumnModel().getColumn(2).setCellRenderer(new ColorRenderer(Color.BLACK, false));

            table.setRowHeight(20);
            table.setShowHorizontalLines(true);
            table.setShowVerticalLines(true);
            table.setIntercellSpacing(new Dimension(1, 1));
            table.setGridColor(AppletWindow.DATA_TABLE_GRID_COLOR);
            table.setSelectionBackground(new Color(217, 235, 245));
            table.setSelectionForeground(Color.BLACK);

            TableComparatorChooser.install(table, sortedEntries, AbstractTableComparatorChooser.MULTIPLE_COLUMN_KEYBOARD);

            issuesSelectionModel = new DefaultEventSelectionModel(filteredEntries);
            issuesSelectionModel.setSelectionMode(ListSelection.MULTIPLE_INTERVAL_SELECTION);
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
        filterPanel.getMatcherEditorDMStatus().fireMatchAllA();
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

            dockingContainer.getUIProcessor().setDockDataChildItems(selectedItem.getDataChildItems());
            dockingContainer.getUIProcessor().setDockObjectTreeObjects(selectedItem.getObjects());
        }
    }

    public EventList<DataMainItem> getDataMainItems() {
        return dataMainItems;
    }

    public void dispose() {
        System.out.println("DockDataMain clear...");

        for (DataMainItem dataMainItem : dataMainItems)
            dataMainItem.getDataChildItems().clear();

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
}
