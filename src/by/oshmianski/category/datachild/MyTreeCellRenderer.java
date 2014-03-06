package by.oshmianski.category.datachild;

import by.oshmianski.objects.DataChildItem;
import by.oshmianski.utils.MyLog;
import ca.odell.glazedlists.TreeList;
import ca.odell.glazedlists.swing.TreeNodeData;
import ca.odell.glazedlists.swing.TreeTableCellPanel;
import ca.odell.glazedlists.swing.TreeTableCellRenderer;
import ca.odell.glazedlists.swing.TreeTableNodeDataRenderer;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class MyTreeCellRenderer extends TreeTableCellRenderer {
    private TreeList treeList;
    private final TreeNodeData treeNodeData = new TreeNodeData();
    private TableCellRenderer delegate;
    private boolean showExpanderForEmptyParent;

    private final TreeTableCellPanel component = new TreeTableCellPanel();

    public MyTreeCellRenderer(TableCellRenderer delegate, TreeList treeList) {
        super(delegate, treeList);
        this.treeList = treeList;
        this.delegate = delegate;
        this.showExpanderForEmptyParent = false;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        DataChildItem item = (DataChildItem) value;

        treeList.getReadWriteLock().readLock().lock();
        try {
            treeNodeData.setDepth(treeList.depth(row));
            treeNodeData.setExpanded(treeList.isExpanded(row));
            treeNodeData.setHasChildren(treeList.hasChildren(row));
            treeNodeData.setAllowsChildren(treeList.getAllowsChildren(row));
        } finally {
            treeList.getReadWriteLock().readLock().unlock();
        }

        if (delegate instanceof TreeTableNodeDataRenderer) {
            ((TreeTableNodeDataRenderer) delegate).setTreeNodeData(treeNodeData);
        }

        final Component c = delegate.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        final int indent = item.getSortLevel() < 0 ? getIndent(treeNodeData, showExpanderForEmptyParent) + 12 : getIndent(treeNodeData, showExpanderForEmptyParent);
        final int spacer = getSpacer(treeNodeData, showExpanderForEmptyParent);
        component.configure(treeNodeData, showExpanderForEmptyParent, c, false, indent, spacer);

        if (hasFocus) {
            component.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(115, 164, 209)),
                    BorderFactory.createLineBorder(new Color(192, 217, 236))));
        }

        return component;
    }

    @Override
    protected int getIndent(TreeNodeData treeNodeData, boolean showExpanderForEmptyParent) {
//        return super.getIndent(treeNodeData, showExpanderForEmptyParent);
        return UIManager.getIcon("Tree.expandedIcon").getIconWidth() * treeNodeData.getDepth();
    }

    public void dispose() {

        try {
//            treeList.dispose();
//        treeNodeData.;
//        delegate.;
        } catch (Exception e) {
            MyLog.add2Log(e);
        }
    }
}
