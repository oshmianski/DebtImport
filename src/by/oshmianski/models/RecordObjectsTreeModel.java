package by.oshmianski.models;

import by.oshmianski.objects.RecordObject;
import by.oshmianski.objects.RecordObjectField;
import by.oshmianski.objects.RecordObjectRoot;
import by.oshmianski.ui.TreeTable.AbstractTreeTableModel;
import by.oshmianski.ui.TreeTable.TreeTableModel;

/**
 * Created with IntelliJ IDEA.
 * User: VintsalovichS
 * Date: 30.09.13
 * Time: 14:32
 */
public class RecordObjectsTreeModel extends AbstractTreeTableModel {
    private String[] cols = {"Объект \\ поле", "Поле (User)", "Тип", "Значение"};

    public RecordObjectsTreeModel(Object root) {
        super(root);
    }

    /**
     * Error in AbstractTreeTableModel !!!
     * Without overriding this method you can't expand the tree!
     */
    public Class getColumnClass(int column) {
        switch (column) {
            case 0:
                return TreeTableModel.class;
            default:
                return Object.class;
        }
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
        switch (column) {
            case 0:
                return node.toString();
            case 1:
                if (node instanceof RecordObjectField) return ((RecordObjectField) node).getTitleUser();
            case 2:
                if (node instanceof RecordObjectField) return ((RecordObjectField) node).getType();
            case 3:
                if (node instanceof RecordObjectField) return ((RecordObjectField) node).getValue();
        }
        return null;
    }

    @Override
    public Object getChild(Object parent, int index) {
        if (parent instanceof RecordObject) {
            return ((RecordObject) parent).getFields().get(index);
        }
        if (parent instanceof RecordObjectRoot) {
            return ((RecordObjectRoot) parent).getObjects().get(index);
        }
        return null;
    }

    @Override
    public int getChildCount(Object parent) {
        if (parent instanceof RecordObject) {
            return ((RecordObject) parent).getFields().size();
        }
        if (parent instanceof RecordObjectRoot) {
            return ((RecordObjectRoot) parent).getObjects().size();
        }
        return 0;
    }

    @Override
    public boolean isCellEditable(Object node, int column) {
        if (column == 3) return true;
        return super.isCellEditable(node, column);
    }
}
