package by.oshmianski.docks;

import by.oshmianski.docks.Setup.DockSimple;
import by.oshmianski.docks.Setup.DockingContainer;
import by.oshmianski.main.AppletWindow;
import by.oshmianski.models.RecordObjectsTreeModel;
import by.oshmianski.objects.*;
import by.oshmianski.ui.TreeTable.*;
import by.oshmianski.ui.utils.ColorRenderer;
import by.oshmianski.ui.utils.niceScrollPane.NiceScrollPane;
import by.oshmianski.utils.IconContainer;
import by.oshmianski.utils.MyLog;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.lang.Object;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: oshmianski
 * Date: 15.09.13
 * Time: 22:14
 */
public class DockObjectTree extends DockSimple {
    private DockingContainer dockingContainer;

    private JTreeTable table;
    private RecordObjectsTreeModel model;
    private RecordObjectRoot root;

    public DockObjectTree(DockingContainer dockingContainer, ArrayList<RecordObject> objects) {
        super("DockObjectTree", IconContainer.getInstance().loadImage("tree.png"), "Дерево объектов");

        this.dockingContainer = dockingContainer;

        JScrollPane sp;

        try {
            root = new RecordObjectRoot("root");
            root.setObjects(objects == null ? (new ArrayList<RecordObject>()) : objects);

            model = new RecordObjectsTreeModel(root);

            table = new JTreeTable(model);
            JTree tree = table.getTree();
            tree.setRootVisible(false);

            table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            table.getColumnModel().getColumn(0).setPreferredWidth(200);
            table.getColumnModel().getColumn(1).setPreferredWidth(300);

            table.getColumnModel().getColumn(1).setCellRenderer(new ColorRenderer(Color.BLACK, false));

            table.setRowHeight(20);
            table.setShowHorizontalLines(true);
            table.setShowVerticalLines(true);
            table.setIntercellSpacing(new Dimension(1, 1));
            table.setGridColor(AppletWindow.DATA_TABLE_GRID_COLOR);
            table.setSelectionBackground(new Color(217, 235, 245));
            table.setSelectionForeground(Color.BLACK);

            sp = new NiceScrollPane(table);

            sp.setBackground(AppletWindow.REPORT_PANEL_BACKGROUND);
            sp.getViewport().setBackground(AppletWindow.REPORT_PANEL_BACKGROUND);

            panel.add(sp);

        } catch (Exception e) {
            MyLog.add2Log(e);
        }
    }

    public void cleareObject() {
        root.setObjects(new ArrayList<RecordObject>());
        model.fireTreeStructureChanged(new TreePath(model.getRoot()));
    }

    public void setObjects(ArrayList<RecordObject> objects) {
        try {
            root.setObjects(objects);
            model.fireTreeStructureChanged(new TreePath(model.getRoot()));
            expandAll(table.getTree(), true);
        } catch (Exception e) {
            MyLog.add2Log(e);
        }
    }

    public void dispose() {
        System.out.println("DockObjectTree clear...");

        if (model != null) model = null;
        if (root != null) root.getObjects().clear();

        System.out.println("DockObjectTree clear...OK");
    }

    // If expand is true, expands all nodes in the tree.
    // Otherwise, collapses all nodes in the tree.
    public void expandAll(JTree tree, boolean expand) {
        Object root = tree.getModel().getRoot();

        // Traverse tree from root
        expandAll(tree, new TreePath(root), expand);
    }

    /**
     * @return Whether an expandPath was called for the last node in the parent path
     */
    private boolean expandAll(JTree tree, TreePath parent, boolean expand) {
        // Traverse children
        RecordObjectRoot node = (RecordObjectRoot) parent.getLastPathComponent();
        if (node.getObjects().size() > 0) {
            boolean childExpandCalled = false;
            for (RecordObject recordObject : node.getObjects()) {
                TreePath path = parent.pathByAddingChild(recordObject);
//                childExpandCalled = expandAll(tree, path, expand) || childExpandCalled; // the OR order is important here, don't let childExpand first. func calls will be optimized out !
                tree.expandPath(path);
            }

//            if (!childExpandCalled) { // only if one of the children hasn't called already expand
            // Expansion or collapse must be done bottom-up, BUT only for non-leaf nodes
            if (expand) {
                tree.expandPath(parent);
            } else {
                tree.collapsePath(parent);
            }
//            }
            return true;
        } else {
            return false;
        }
    }
}
