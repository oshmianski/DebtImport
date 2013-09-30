package by.oshmianski.docks;

import by.oshmianski.category.datachild.*;
import by.oshmianski.docks.Setup.DockSimple;
import by.oshmianski.docks.Setup.DockingContainer;
import by.oshmianski.filter.CM.FilterPanelChild;
import by.oshmianski.main.AppletWindow;
import by.oshmianski.models.DataChildModel;
import by.oshmianski.objects.DataChildItem;
import by.oshmianski.objects.RecordObject;
import by.oshmianski.test.TreeTable.JTreeTable;
import by.oshmianski.test.TreeTable.MyTreeModel;
import by.oshmianski.test.TreeTable.TreeTableModelAdapter;
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
import javax.swing.tree.DefaultMutableTreeNode;
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
public class DockObjectTree extends DockSimple {
    private DockingContainer dockingContainer;
    private EventList<RecordObject> objects;

//    private DefaultEventTableModel model;
    private EventList<RecordObject> entries;
    private SortedList<RecordObject> sortedEntries;
    private JTreeTable table;

    public DockObjectTree(DockingContainer dockingContainer, EventList<RecordObject> objects) {
        super("DockObjectTree", IconContainer.getInstance().loadImage("layers.png"), "Дерево объектов");

        this.dockingContainer = dockingContainer;
        this.objects = (objects == null ? (new BasicEventList<RecordObject>()) : objects);

//        table = new JTreeTable(null);

        JScrollPane sp;

        this.objects.getReadWriteLock().writeLock().lock();

        try {
            entries = GlazedListsSwing.swingThreadProxyList(this.objects);

            sortedEntries = new SortedList<RecordObject>(entries, GlazedLists.chainComparators(
                    GlazedLists.beanPropertyComparator(RecordObject.class, "title")
            ));

            DefaultMutableTreeNode root = new DefaultMutableTreeNode("0");
            DefaultMutableTreeNode child1 = new DefaultMutableTreeNode("1");
            DefaultMutableTreeNode child11 = new DefaultMutableTreeNode("11");
            DefaultMutableTreeNode child12 = new DefaultMutableTreeNode("12");
            child1.add(child11);
            child1.add(child12);
            root.add(child1);

            MyTreeModel model1 = new MyTreeModel(root);

            table = new JTreeTable(model1);

//            table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//            table.getColumnModel().getColumn(0).setPreferredWidth(100);
//            table.getColumnModel().getColumn(1).setPreferredWidth(300);
//            table.getColumnModel().getColumn(2).setPreferredWidth(300);

//            table.getColumnModel().getColumn(0).setCellRenderer(new StatusRenderer(false, -1));
//            table.getColumnModel().getColumn(1).setCellRenderer(new ColorRenderer(Color.BLACK, false));
//            table.getColumnModel().getColumn(2).setCellRenderer(new ColorRenderer(Color.BLUE, false));

//            table.setRowHeight(20);
//            table.setShowHorizontalLines(true);
//            table.setShowVerticalLines(true);
//            table.setIntercellSpacing(new Dimension(1, 1));
//            table.setGridColor(AppletWindow.DATA_TABLE_GRID_COLOR);
//            table.setSelectionBackground(new Color(217, 235, 245));
//            table.setSelectionForeground(Color.BLACK);

            sp = new NiceScrollPane(table);

            sp.setBackground(AppletWindow.REPORT_PANEL_BACKGROUND);
            sp.getViewport().setBackground(AppletWindow.REPORT_PANEL_BACKGROUND);

            panel.add(sp);

        } catch (Exception e) {
            MyLog.add2Log(e);
        } finally {
            this.objects.getReadWriteLock().writeLock().unlock();
        }
    }

    public void clearObjects() {
        objects.clear();
    }

    public void setObjects(ArrayList<RecordObject> objs) {
        objects.getReadWriteLock().writeLock().lock();
        try {
            if (objects == null) {
                objects.clear();
            } else {
                objects.clear();
                objects.addAll(objs);
            }
        } catch (Exception e) {
            MyLog.add2Log(e);
        } finally {
            objects.getReadWriteLock().writeLock().unlock();
        }
    }

    public void dispose() {
        System.out.println("DockObjectTree clear...");

//        if (model != null) model = null;
        if (sortedEntries != null) sortedEntries.dispose();
        if (entries != null) entries.dispose();
        if (objects != null) objects.dispose();

        System.out.println("DockObjectTree clear...OK");
    }
}
