package by.oshmianski.test.TreeTable;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Created with IntelliJ IDEA.
 * User: VintsalovichS
 * Date: 30.09.13
 * Time: 14:32
 */
public class MyTreeModel extends AbstractTreeTableModel{
    private String[] cols = {"Объект \\ поле", "Значение"};
    public MyTreeModel(Object root) {
        super(root);
    }

    @Override
    public int getColumnCount() {
        return cols.length;
    }

    @Override
    public String getColumnName(int column) {
        return cols[column];
    }

    @Override
    public Object getValueAt(Object node, int column) {
        return node.toString();
    }

    @Override
    public Object getChild(Object parent, int index) {
        return ((DefaultMutableTreeNode) parent).getChildAt(index);
    }

    @Override
    public int getChildCount(Object parent) {
        System.out.println("((DefaultMutableTreeNode) parent).getChildCount()="+((DefaultMutableTreeNode) parent).getChildCount());
        return ((DefaultMutableTreeNode) parent).getChildCount();
    }
}
