package by.oshmianski.ui.utils;

import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.util.Enumeration;
import java.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: 8-058
 * Date: 15.06.12
 * Time: 9:43
 * To change this template use File | Settings | File Templates.
 */
public class XTableColumnModel {
    TableColumnModel columnModel;
    /**
     * Array of TableColumn objects in this model.
     * Holds all column objects, regardless of their visibility
     */
    protected Vector allTableColumns = new Vector();
//    protected Vector tableColumns = new Vector();

    /**
     * Creates an extended table column model.
     */
    public XTableColumnModel(TableColumnModel columnModel) {
        this.columnModel = columnModel;
        for (int i = 0; i < this.columnModel.getColumnCount(); i++) {
//            tableColumns.add(this.columnModel.getColumn(i));
            allTableColumns.add(this.columnModel.getColumn(i));
        }
    }

    /**
     * Sets the visibility of the specified TableColumn.
     * The call is ignored if the TableColumn is not found in this column model
     * or its visibility status did not change.
     * <p/>
     *
     * @param column  the column to show/hide
     * @param visible its new visibility status
     */
    // listeners will receive columnAdded()/columnRemoved() event
    public void setColumnVisible(TableColumn column, boolean visible) {
        if (!visible) {
            columnModel.removeColumn(column);
        } else {
            // find the visible index of the column:
            // iterate through both collections of visible and all columns, counting
            // visible columns up to the one that's about to be shown again
//            int noVisibleColumns = tableColumns.size();
            int noVisibleColumns = columnModel.getColumnCount();
            int noInvisibleColumns = allTableColumns.size();
            int visibleIndex = 0;

            for (int invisibleIndex = 0; invisibleIndex < noInvisibleColumns; ++invisibleIndex) {
//                TableColumn visibleColumn = (visibleIndex < noVisibleColumns ? (TableColumn) tableColumns.get(visibleIndex) : null);
                TableColumn visibleColumn = (visibleIndex < noVisibleColumns ? columnModel.getColumn(visibleIndex) : null);
                TableColumn testColumn = (TableColumn) allTableColumns.get(invisibleIndex);

                if (testColumn == column) {
                    if (visibleColumn != column) {
                        columnModel.addColumn(column);
//                        columnModel.moveColumn(tableColumns.size() - 1, visibleIndex);
                        columnModel.moveColumn(columnModel.getColumnCount() - 1, visibleIndex);
                    }
                    return; // ####################
                }
                if (testColumn == visibleColumn) {
                    ++visibleIndex;
                }
            }
        }
    }

    /**
     * Makes all columns in this model visible
     */
    public void setAllColumnsVisible() {
        int noColumns = allTableColumns.size();

        for (int columnIndex = 0; columnIndex < noColumns; ++columnIndex) {
//            TableColumn visibleColumn = (columnIndex < tableColumns.size() ? (TableColumn) tableColumns.get(columnIndex) : null);
            TableColumn visibleColumn = (columnIndex < columnModel.getColumnCount() ? columnModel.getColumn(columnIndex) : null);
            TableColumn invisibleColumn = (TableColumn) allTableColumns.get(columnIndex);

            if (visibleColumn != invisibleColumn) {
                columnModel.addColumn(invisibleColumn);
                columnModel.moveColumn(columnModel.getColumnCount() - 1, columnIndex);
            }
        }
    }

    /**
     * Maps the index of the column in the table model at
     * <code>modelColumnIndex</code> to the TableColumn object.
     * There may me multiple TableColumn objects showing the same model column, though this is uncommon.
     * This method will always return the first visible or else the first invisible column with the specified index.
     *
     * @param modelColumnIndex index of column in table model
     * @return table column object or null if no such column in this column model
     */
    public TableColumn getColumnByModelIndex(int modelColumnIndex) {
        for (int columnIndex = 0; columnIndex < allTableColumns.size(); ++columnIndex) {
            TableColumn column = (TableColumn) allTableColumns.elementAt(columnIndex);
            if (column.getModelIndex() == modelColumnIndex) {
                return column;
            }
        }
        return null;
    }

    /**
     * Checks wether the specified column is currently visible.
     *
     * @param aColumn column to check
     * @return visibility of specified column (false if there is no such column at all. [It's not visible, right?])
     */
//    public boolean isColumnVisible(TableColumn aColumn) {
//        return (tableColumns.indexOf(aColumn) >= 0);
//    }

    /**
     * Append <code>column</code> to the right of exisiting columns.
     * Posts <code>columnAdded</code> event.
     *
     * @param column The column to be added
     * @throws IllegalArgumentException if <code>column</code> is <code>null</code>
     * @see #removeColumn
     */
    public void addColumn(TableColumn column) {
        allTableColumns.addElement(column);
        columnModel.addColumn(column);
    }

    /**
     * Removes <code>column</code> from this column model.
     * Posts <code>columnRemoved</code> event.
     * Will do nothing if the column is not in this model.
     *
     * @param column the column to be added
     * @see #addColumn
     */
    public void removeColumn(TableColumn column) {
        int allColumnsIndex = allTableColumns.indexOf(column);
        if (allColumnsIndex != -1) {
            allTableColumns.removeElementAt(allColumnsIndex);
        }
        columnModel.removeColumn(column);
    }

    /**
     * Moves the column from <code>oldIndex</code> to <code>newIndex</code>.
     * Posts  <code>columnMoved</code> event.
     * Will not move any columns if <code>oldIndex</code> equals <code>newIndex</code>.
     *
     * @param oldIndex index of column to be moved
     * @param newIndex new index of the column
     * @throws IllegalArgumentException if either <code>oldIndex</code> or
     *                                  <code>newIndex</code>
     *                                  are not in [0, getColumnCount() - 1]
     */
    public void moveColumn(int oldIndex, int newIndex) {
        if ((oldIndex < 0) || (oldIndex >= columnModel.getColumnCount()) ||
                (newIndex < 0) || (newIndex >= columnModel.getColumnCount()))
            throw new IllegalArgumentException("moveColumn() - Index out of range");

//        TableColumn fromColumn = (TableColumn) tableColumns.get(oldIndex);
        TableColumn fromColumn = columnModel.getColumn(oldIndex);
//        TableColumn toColumn = (TableColumn) tableColumns.get(newIndex);
        TableColumn toColumn = columnModel.getColumn(newIndex);

        int allColumnsOldIndex = allTableColumns.indexOf(fromColumn);
        int allColumnsNewIndex = allTableColumns.indexOf(toColumn);

        if (oldIndex != newIndex) {
            allTableColumns.removeElementAt(allColumnsOldIndex);
            allTableColumns.insertElementAt(fromColumn, allColumnsNewIndex);
        }

        columnModel.moveColumn(oldIndex, newIndex);
    }

    /**
     * Returns the total number of columns in this model.
     *
     * @param onlyVisible if set only visible columns will be counted
     * @return the number of columns in the <code>tableColumns</code> array
     * @see #getColumns
     */
//    public int getColumnCount(boolean onlyVisible) {
//        Vector columns = (onlyVisible ? tableColumns : allTableColumns);
//        return columns.size();
//    }

    /**
     * Returns an <code>Enumeration</code> of all the columns in the model.
     *
     * @param onlyVisible if set all invisible columns will be missing from the enumeration.
     * @return an <code>Enumeration</code> of the columns in the model
     */
    public Enumeration getColumns(boolean onlyVisible) {
//        Vector columns = (onlyVisible ? tableColumns : allTableColumns);
//
//        return columns.elements();
        if (onlyVisible) {
            return columnModel.getColumns();
        } else {
            return allTableColumns.elements();
        }
    }

    /**
     * Returns the position of the first column whose identifier equals <code>identifier</code>.
     * Position is the the index in all visible columns if <code>onlyVisible</code> is true or
     * else the index in all columns.
     *
     * @param identifier  the identifier object to search for
     * @param onlyVisible if set searches only visible columns
     * @return the index of the first column whose identifier
     *         equals <code>identifier</code>
     * @throws IllegalArgumentException if <code>identifier</code>
     *                                  is <code>null</code>, or if no
     *                                  <code>TableColumn</code> has this
     *                                  <code>identifier</code>
     * @see #getColumn
     */
//    public int getColumnIndex(Object identifier, boolean onlyVisible) {
//        if (identifier == null) {
//            throw new IllegalArgumentException("Identifier is null");
//        }
//
//        Vector columns = (onlyVisible ? tableColumns : allTableColumns);
//        int noColumns = columns.size();
//        TableColumn column;
//
//        for (int columnIndex = 0; columnIndex < noColumns; ++columnIndex) {
//            column = (TableColumn) columns.get(columnIndex);
//
//            if (identifier.equals(column.getIdentifier()))
//                return columnIndex;
//        }
//
//        throw new IllegalArgumentException("Identifier not found");
//    }

    /**
     * Returns the <code>TableColumn</code> object for the column
     * at <code>columnIndex</code>.
     *
     * @param columnIndex the index of the column desired
     * @param onlyVisible if set columnIndex is meant to be relative to all visible columns only
     *                    else it is the index in all columns
     * @return the <code>TableColumn</code> object for the column
     *         at <code>columnIndex</code>
     */
    public TableColumn getColumn(int columnIndex, boolean onlyVisible) {
//        return (TableColumn) tableColumns.elementAt(columnIndex);
//        return (TableColumn) columnModel.getColumn(columnIndex);
        return (TableColumn) allTableColumns.elementAt(columnIndex);
    }

    public TableColumn getColumnVisible(int columnIndex, boolean onlyVisible) {
//        return (TableColumn) tableColumns.elementAt(columnIndex);
        return columnModel.getColumn(columnIndex);
//        return (TableColumn) allTableColumns.elementAt(columnIndex);
    }
}