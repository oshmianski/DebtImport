package by.oshmianski.ui.utils;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.EventObject;

/**
 * Created with IntelliJ IDEA.
 * User: 8-058
 * Date: 25.04.13
 * Time: 17:06
 */
public class BetterJTableEdittSelectAll extends BetterJTable {
    private boolean isSelectAllForMouseEvent = false;
    private boolean isSelectAllForActionEvent = false;
    private boolean isSelectAllForKeyEvent = false;

    public BetterJTableEdittSelectAll(TableModel dm) {
        super(dm);
    }


    /*
 *  Override to provide Select All editing functionality
 */
    public boolean editCellAt(int row, int column, EventObject e) {
        boolean result = super.editCellAt(row, column, e);

        if (isSelectAllForMouseEvent
                || isSelectAllForActionEvent
                || isSelectAllForKeyEvent) {
            selectAll(e);
        }

        return result;
    }

    /*
     * Select the text when editing on a text related cell is started
     */
    private void selectAll(EventObject e) {
        final Component editor = getEditorComponent();

        if (editor == null
//                || !(editor instanceof JTextComponent)
                || !(editor instanceof JComboBox)
                )
            return;

        if (e == null) {
            ((JComboBox) editor).getEditor().selectAll();
            return;
        }

        //  Typing in the cell was used to activate the editor

        if (e instanceof KeyEvent && isSelectAllForKeyEvent) {
            ((JComboBox) editor).getEditor().selectAll();
            return;
        }

        //  F2 was used to activate the editor

        if (e instanceof ActionEvent && isSelectAllForActionEvent) {
            ((JComboBox) editor).getEditor().selectAll();
            return;
        }

        //  A mouse click was used to activate the editor.
        //  Generally this is a double click and the second mouse click is
        //  passed to the editor which would remove the text selection unless
        //  we use the invokeLater()

        if (e instanceof MouseEvent && isSelectAllForMouseEvent) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    ((JComboBox) editor).getEditor().selectAll();
                }
            });
        }
    }

    //
//  Newly added methods
//
    /*
	 *  Sets the Select All property for for all event types
	 */
    public void setSelectAllForEdit(boolean isSelectAllForEdit) {
        setSelectAllForMouseEvent(isSelectAllForEdit);
        setSelectAllForActionEvent(isSelectAllForEdit);
        setSelectAllForKeyEvent(isSelectAllForEdit);
    }

    /*
     *  Set the Select All property when editing is invoked by the mouse
     */
    public void setSelectAllForMouseEvent(boolean isSelectAllForMouseEvent) {
        this.isSelectAllForMouseEvent = isSelectAllForMouseEvent;
    }

    /*
     *  Set the Select All property when editing is invoked by the "F2" key
     */
    public void setSelectAllForActionEvent(boolean isSelectAllForActionEvent) {
        this.isSelectAllForActionEvent = isSelectAllForActionEvent;
    }

    /*
     *  Set the Select All property when editing is invoked by
     *  typing directly into the cell
     */
    public void setSelectAllForKeyEvent(boolean isSelectAllForKeyEvent) {
        this.isSelectAllForKeyEvent = isSelectAllForKeyEvent;
    }

//
//  Static, convenience methods
//

    /**
     * Convenience method to order the table columns of a table. The columns
     * are ordered based on the column names specified in the array. If the
     * column name is not found then no column is moved. This means you can
     * specify a null value to preserve the current order of a given column.
     *
     * @param table       the table containing the columns to be sorted
     * @param columnNames an array containing the column names in the
     *                    order they should be displayed
     */
    public static void reorderColumns(JTable table, Object... columnNames) {
        TableColumnModel model = table.getColumnModel();

        for (int newIndex = 0; newIndex < columnNames.length; newIndex++) {
            try {
                Object columnName = columnNames[newIndex];
                int index = model.getColumnIndex(columnName);
                model.moveColumn(index, newIndex);
            } catch (IllegalArgumentException e) {
            }
        }
    }
}
