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
    private final static String dockTitle = "Дерево объектов";

    public DockObjectTree(DockingContainer dockingContainer, ArrayList<RecordObject> objects) {
        super("DockObjectTree", IconContainer.getInstance().loadImage("tree.png"), dockTitle);

        this.dockingContainer = dockingContainer;

        JScrollPane sp;

        try {
            root = new RecordObjectRoot("root");
            root.setObjects(objects == null ? (new ArrayList<RecordObject>()) : objects);

            model = new RecordObjectsTreeModel(root);

            table = new JTreeTable(model);
            JTree tree = table.getTree();
            tree.setRootVisible(false);
            tree.setCellRenderer(new ObjectTreeRender());

            table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            table.getColumnModel().getColumn(0).setPreferredWidth(200);
            table.getColumnModel().getColumn(1).setPreferredWidth(150);
            table.getColumnModel().getColumn(2).setPreferredWidth(70);
            table.getColumnModel().getColumn(3).setPreferredWidth(300);

            table.getColumnModel().getColumn(1).setCellRenderer(new ColorRenderer(Color.BLUE, false));
            table.getColumnModel().getColumn(2).setCellRenderer(new ColorRenderer(Color.BLACK, false));
            table.getColumnModel().getColumn(3).setCellRenderer(new ColorRenderer(Color.BLUE, false));

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

    public void setObjects(DataMainItem dataMainItem) {
        ArrayList<RecordObject> objects = dataMainItem.getObjects();

        setTitleText(dockTitle + " :: " + dataMainItem.getDescription());

        try {
            root.setObjects(objects);
            model.fireTreeStructureChanged(new TreePath(model.getRoot()));
            expandFirstLevel(table.getTree(), true);
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

    public void expandFirstLevel(JTree tree, boolean expand) {
        Object root = tree.getModel().getRoot();

        expandFirstLevel(tree, new TreePath(root), expand);
    }

    private void expandFirstLevel(JTree tree, TreePath parent, boolean expand) {
        RecordObjectRoot node = (RecordObjectRoot) parent.getLastPathComponent();
        if (node.getObjects().size() > 0) {
            for (RecordObject recordObject : node.getObjects()) {
                TreePath path = parent.pathByAddingChild(recordObject);
                tree.expandPath(path);
            }

            if (expand) {
                tree.expandPath(parent);
            } else {
                tree.collapsePath(parent);
            }
        }
    }

    private static class ObjectTreeRender extends DefaultTreeCellRenderer {
        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            Component c = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

            JLabel label = (JLabel) c;

            if (value instanceof RecordObject) {
                c.setFont(new Font("tahoma", Font.BOLD, 11));

                label.setForeground(new Color(0x176317));

                if (((RecordObject) value).isFlagEmpty()) {
                    label.setForeground(new Color(0xE80000));
                }

                if (((RecordObject) value).isExistInDB()) {
                    label.setForeground(new Color(0x0032A0));
                }

                if (((RecordObject) value).isExistInPrevios()) {
                    label.setForeground(new Color(0xCB7F2A));
                }
            } else {
                c.setFont(new Font("tahoma", Font.PLAIN, 11));
                label.setIcon(null);
            }

            return c;
        }
    }
}
