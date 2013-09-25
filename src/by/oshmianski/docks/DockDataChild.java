package by.oshmianski.docks;

import by.oshmianski.category.datachild.*;
import by.oshmianski.docks.Setup.DockSimple;
import by.oshmianski.docks.Setup.DockingContainer;
import by.oshmianski.filter.CM.FilterPanelChild;
import by.oshmianski.main.AppletWindow;
import by.oshmianski.models.DataChildModel;
import by.oshmianski.objects.DataChildItem;
import by.oshmianski.ui.utils.BetterJTable;
import by.oshmianski.ui.utils.ColorRenderer;
import by.oshmianski.ui.utils.StatusRenderer;
import by.oshmianski.ui.utils.niceScrollPane.NiceScrollPane;
import by.oshmianski.utils.IconContainer;
import by.oshmianski.utils.MyLog;
import ca.odell.glazedlists.*;
import ca.odell.glazedlists.matchers.Matchers;
import ca.odell.glazedlists.swing.DefaultEventTableModel;
import ca.odell.glazedlists.swing.GlazedListsSwing;
import ca.odell.glazedlists.swing.TreeTableSupport;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: oshmianski
 * Date: 15.09.13
 * Time: 22:14
 */
public class DockDataChild extends DockSimple {
    private DockingContainer dockingContainer;
    private EventList<DataChildItem> dataChildItems;

    private DefaultEventTableModel model;
    private EventList<DataChildItem> entries;
    private SortedList<DataChildItem> sortedEntries;
    private FilterList<DataChildItem> filteredUls;
    private JTable table;
    private FilterPanelChild filterPanel;

    private TreeTableSupport treeTableSupport;
    private EventList ALL_CRITERIA = new BasicEventList();
    private List critetionList;
    private DataChildItemTreeFormat treeFormatCriterion;
    private TreeList treeList;
    private ItemCatSumsAll itemCatSumsAll;
    private Map<Integer, ItemCats> listCat = new HashMap<Integer, ItemCats>();
    private EventList activeCriteria;

    public DockDataChild(DockingContainer dockingContainer, EventList<DataChildItem> dataChildItems) {
        super("DockDataChild", IconContainer.getInstance().loadImage("layers.png"), "Расшифровка данных");

        this.dockingContainer = dockingContainer;
        this.dataChildItems = (dataChildItems == null ? (new BasicEventList<DataChildItem>()) : dataChildItems);

        ALL_CRITERIA.add(new CriterionObject());

        activeCriteria = new FilterList(ALL_CRITERIA, Matchers.beanPropertyMatcher(TreeCriterion.class, "active", Boolean.TRUE));
        critetionList = new ArrayList(activeCriteria);

        treeFormatCriterion = new DataChildItemTreeFormat(critetionList);

        table = new BetterJTable(null);

        JScrollPane sp;

        this.dataChildItems.getReadWriteLock().writeLock().lock();

        try {
            entries = GlazedListsSwing.swingThreadProxyList(this.dataChildItems);

            filterPanel = new FilterPanelChild(entries, true);

            sortedEntries = new SortedList<DataChildItem>(entries, GlazedLists.chainComparators(
                    GlazedLists.beanPropertyComparator(DataChildItem.class, "object")
            ));

            filteredUls = new FilterList<DataChildItem>(sortedEntries, filterPanel.getMatcherEditor());

            itemCatSumsAll = new ItemCatSumsAll(filteredUls, listCat, critetionList);
            itemCatSumsAll.Install();

            treeList = new TreeList<DataChildItem>(filteredUls, treeFormatCriterion, TreeList.NODES_START_EXPANDED);

            model = new DefaultEventTableModel(treeList, new DataChildModel(listCat));

            filterPanel.install(model);

            table.setModel(model);

            table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            table.getColumnModel().getColumn(0).setPreferredWidth(100);
            table.getColumnModel().getColumn(1).setPreferredWidth(300);
            table.getColumnModel().getColumn(2).setPreferredWidth(300);

            table.getColumnModel().getColumn(0).setCellRenderer(new StatusRenderer(false, -1));
            table.getColumnModel().getColumn(1).setCellRenderer(new ColorRenderer(Color.BLACK, false));
            table.getColumnModel().getColumn(2).setCellRenderer(new ColorRenderer(Color.BLUE, false));

            table.setRowHeight(20);
            table.setShowHorizontalLines(true);
            table.setShowVerticalLines(true);
            table.setIntercellSpacing(new Dimension(1, 1));
            table.setGridColor(AppletWindow.DATA_TABLE_GRID_COLOR);
            table.setSelectionBackground(new Color(217, 235, 245));
            table.setSelectionForeground(Color.BLACK);

            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    treeTableSupport = TreeTableSupport.install(table, treeList, 1);
                    treeTableSupport.setRenderer(new MyTreeCellRenderer(new TreeRenderer(), treeList));
                }
            });

            sp = new NiceScrollPane(table);

            sp.setBackground(AppletWindow.REPORT_PANEL_BACKGROUND);
            sp.getViewport().setBackground(AppletWindow.REPORT_PANEL_BACKGROUND);

            panel.add(sp);
            dockingContainer.setDockDataChildFilter(filterPanel);

        } catch (Exception e) {
            MyLog.add2Log(e);
        } finally {
            this.dataChildItems.getReadWriteLock().writeLock().unlock();
        }
    }

    public void clearDataChild() {
        dataChildItems.clear();
        filterPanel.getMatcherEditorCMStatus().fireMatchAllA();
    }

    public void setDataChildItems(ArrayList<DataChildItem> dataChildItemEventList) {
        dataChildItems.getReadWriteLock().writeLock().lock();
        try {
            if (dataChildItemEventList == null) {
                dataChildItems.clear();
                filterPanel.getMatcherEditorCMStatus().fireMatchAllA();
            } else {
                dataChildItems.clear();
                filterPanel.getMatcherEditorCMStatus().fireMatchAllA();
                dataChildItems.addAll(dataChildItemEventList);
            }
        } catch (Exception e) {
            MyLog.add2Log(e);
        } finally {
            dataChildItems.getReadWriteLock().writeLock().unlock();
        }
    }

    public void dispose() {
        System.out.println("DockDataChild cleear...");
        if (model != null) model = null;
        if (filteredUls != null) filteredUls.dispose();
        if (sortedEntries != null) sortedEntries.dispose();
        if (entries != null) entries.dispose();
        if (dataChildItems != null) dataChildItems.dispose();

        if (critetionList != null) critetionList = null;
        if (treeFormatCriterion != null) treeFormatCriterion = null;
        if (treeList != null) treeList.dispose();
        if (itemCatSumsAll != null) itemCatSumsAll.dispose();
        if (listCat != null) listCat.clear();
        if (ALL_CRITERIA != null) ALL_CRITERIA.dispose();
        if (activeCriteria != null) activeCriteria.dispose();

        filterPanel.dispose();
        System.out.println("DockDataChild cleear...OK");
    }
}
