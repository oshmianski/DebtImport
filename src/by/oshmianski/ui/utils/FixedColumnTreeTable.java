/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package by.oshmianski.ui.utils;

import by.oshmianski.main.AppletWindow;
import ca.odell.glazedlists.swing.DefaultEventTableModel;
import ca.odell.glazedlists.swing.EventTableColumnModel;
import ca.odell.glazedlists.swing.EventTableModel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 * @author 8-058
 */
public class FixedColumnTreeTable implements ChangeListener, PropertyChangeListener {

    private JTable main;
    private JTable fixed;
    private JScrollPane scrollPane;
    private DefaultEventTableModel model;
    /** Number of frozen columns. */
    private int frozenColumns = 1;

    /* 
     *  Specify the number of columns to be fixed and the scroll pane 
     *  containing the table. 
     */
    public FixedColumnTreeTable(int fixedColumns, JScrollPane scrollPane, DefaultEventTableModel model) {
        this.scrollPane = scrollPane;
        this.model = model;

        main = ((JTable) scrollPane.getViewport().getView());
//      main.setAutoCreateColumnsFromModel( false );  
        main.addPropertyChangeListener(this);

        //  Use the existing table to create a new table sharing  
        //  the DataModel and ListSelectionModel  

//      int totalColumns = main.getColumnCount();

        MouseAdapter ma = new MouseAdapter() {

            TableColumn column;
            int columnWidth;
            int pressedX;

            public void mousePressed(MouseEvent e) {
                JTableHeader header = (JTableHeader) e.getComponent();
                TableColumnModel tcm = header.getColumnModel();
                int columnIndex = tcm.getColumnIndexAtX(e.getX());
                Cursor cursor = header.getCursor();

                if (columnIndex == tcm.getColumnCount() - 1
                        && cursor == Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR)) {
                    column = tcm.getColumn(columnIndex);
                    columnWidth = column.getWidth();
                    pressedX = e.getX();
                    header.addMouseMotionListener(this);
                }
            }

            public void mouseReleased(MouseEvent e) {
                JTableHeader header = (JTableHeader) e.getComponent();
                header.removeMouseMotionListener(this);
            }

            public void mouseDragged(MouseEvent e) {
                int width = columnWidth - pressedX + e.getX();
                column.setPreferredWidth(width);
                JTableHeader header = (JTableHeader) e.getComponent();
                JTable table = header.getTable();
                table.setPreferredScrollableViewportSize(table.getPreferredSize());
                JScrollPane scrollPane = (JScrollPane) table.getParent().getParent();
                scrollPane.revalidate();
            }
        };

        fixed = new JTable(null) {

            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component returnComp = super.prepareRenderer(renderer, row, column);
                Color alternateColor = new Color(247, 247, 247);
                Color whiteColor = Color.WHITE;
                if (returnComp.getBackground() != null) {
                    if (!returnComp.getBackground().equals(getSelectionBackground())) {
                        Color bg = (row % 2 == 0 ? alternateColor : whiteColor);
                        returnComp.setBackground(bg);
//                        bg = null;
                    }
                }
                return returnComp;
            }
        };
        fixed.setAutoCreateColumnsFromModel(false);
        fixed.setModel(model);
        fixed.setSelectionModel(main.getSelectionModel());
        fixed.setFocusable(true);
        fixed.getTableHeader().addMouseListener(ma);

        //  Remove the fixed columns from the main table  
        //  and add them to the fixed table  

//        System.out.println("fixedColumns=" + fixedColumns);
        for (int i = 0; i < fixedColumns; i++) {
            TableColumnModel columnModel = main.getColumnModel();
            TableColumn column = columnModel.getColumn(0);
            columnModel.removeColumn(column);
            fixed.getColumnModel().addColumn(column);
        }

        //  Add the fixed table to the scroll pane  

        fixed.setPreferredScrollableViewportSize(fixed.getPreferredSize());
        scrollPane.setRowHeaderView(fixed);
        scrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER, fixed.getTableHeader());

        scrollPane.getRowHeader().setBackground(AppletWindow.REPORT_PANEL_BACKGROUND);

        // Synchronize scrolling of the row header with the main table

        adjustActionMaps();

        scrollPane.getRowHeader().addChangeListener(this);
    }

    /* 
     *  Return the table being used in the row header 
     */
    public JTable getFixedTable() {
        return fixed;
    }
//  
//  Implement the ChangeListener  
//  

    public void stateChanged(ChangeEvent e) {
        //  Sync the scroll pane scrollbar with the row header  

        JViewport viewport = (JViewport) e.getSource();
        scrollPane.getVerticalScrollBar().setValue(viewport.getViewPosition().y);
    }
//  
//  Implement the PropertyChangeListener  
//  

    public void propertyChange(PropertyChangeEvent e) {
        //  Keep the fixed table in sync with the main table  

        if ("selectionModel".equals(e.getPropertyName())) {
            fixed.setSelectionModel(main.getSelectionModel());
        }

        if ("model".equals(e.getPropertyName())) {
//            fixed.setModel(main.getModel());
            fixed.setModel(model);
        }
    }

    /**
     * Adjusts the action maps of the two tables to allow proper navigation
     * between the two tables.
     */
    private void adjustActionMaps() {
        // collect original left/right/first/last actions
        final Action scrollNextAction = main.getActionMap().get("selectNextColumnCell");
        final Action lockedNextAction = fixed.getActionMap().get("selectNextColumnCell");
        final Action scrollPreviousAction = main.getActionMap().get("selectPreviousColumnCell");
        final Action lockedPreviousAction = fixed.getActionMap().get("selectPreviousColumnCell");
        final Action scrollFirstAction = main.getActionMap().get("selectFirstColumn");
        final Action lockedLastAction = fixed.getActionMap().get("selectLastColumn");
        // set left and right actions
        fixed.getActionMap().put("selectNextColumn", new LockedNextAction(lockedNextAction));
        fixed.getActionMap().put("selectPreviousColumn", new LockedPreviousAction(lockedPreviousAction));
        main.getActionMap().put("selectNextColumn", new ScrollNextAction(scrollNextAction));
        main.getActionMap().put("selectPreviousColumn", new ScrollPreviousAction(scrollPreviousAction));
        // set tab and shift-tab actions
        fixed.getActionMap().put("selectNextColumnCell", new LockedNextAction(lockedNextAction));
        fixed.getActionMap().put("selectPreviousColumnCell", new LockedPreviousAction(lockedPreviousAction));
        main.getActionMap().put("selectNextColumnCell", new ScrollNextAction(scrollNextAction));
        main.getActionMap().put("selectPreviousColumnCell", new ScrollPreviousAction(scrollPreviousAction));
        // set top and end actions
        main.getActionMap().put("selectFirstColumn", new ScrollFirstAction(scrollFirstAction));
        fixed.getActionMap().put("selectLastColumn", new LockedLastAction(lockedLastAction));
    }

    /**
     * Action of <tt>Next</tt> key events in locked table.
     */
    private final class LockedNextAction extends AbstractAction {
        /**
         * the original action.
         */
        private final Action originalAction;

        /**
         * Contructor.
         *
         * @param theOriginalAction the original action
         */
        private LockedNextAction(final Action theOriginalAction) {
            super();
            this.originalAction = theOriginalAction;
        }

        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(final ActionEvent e) {
            if (fixed.getSelectedColumn() == fixed.getColumnCount() - 1 && main.getColumnCount() > 0) {
                fixed.transferFocus();
                main.changeSelection(fixed.getSelectedRow(), 0, false, false);
            } else {
                originalAction.actionPerformed(e);
            }
        }
    }

    /**
     * Action of <tt>Previous</tt> key events in locked table.
     */
    private final class LockedPreviousAction extends AbstractAction {
        /**
         * the original action.
         */
        private final Action originalAction;

        /**
         * Contructor.
         *
         * @param theOriginalAction the original action
         */
        private LockedPreviousAction(final Action theOriginalAction) {
            super();
            this.originalAction = theOriginalAction;
        }

        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(final ActionEvent e) {
            if (fixed.getSelectedColumn() == 0 && main.getColumnCount() > 0) {
                fixed.transferFocus();
                main.changeSelection(previousRow(main), main.getColumnCount() - 1, false, false);
            } else {
                originalAction.actionPerformed(e);
            }
        }
    }

    /**
     * Action of <tt>Next</tt> key events in scrollable table.
     */
    private final class ScrollNextAction extends AbstractAction {
        /**
         * the original action.
         */
        private final Action originalAction;

        /**
         * Contructor.
         *
         * @param theOriginalAction the original action
         */
        private ScrollNextAction(final Action theOriginalAction) {
            super();
            this.originalAction = theOriginalAction;
        }

        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(final ActionEvent e) {
            if (main.getSelectedColumn() == main.getColumnCount() - 1 && fixed.getColumnCount() > 0) {
                main.transferFocusBackward();
                fixed.changeSelection(nextRow(main), 0, false, false);
            } else {
                originalAction.actionPerformed(e);
            }
        }
    }

    /**
     * Action of <tt>Previous</tt> key events in scrollable table.
     */
    private final class ScrollPreviousAction extends AbstractAction {
        /**
         * the original action.
         */
        private final Action originalAction;

        /**
         * Contructor.
         *
         * @param theOriginalAction the original action
         */
        private ScrollPreviousAction(final Action theOriginalAction) {
            super();
            this.originalAction = theOriginalAction;
        }

        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(final ActionEvent e) {
            if (main.getSelectedColumn() == 0 && frozenColumns > 0) {
                main.transferFocusBackward();
                fixed.changeSelection(main.getSelectedRow(), fixed.getColumnCount() - 1, false, false);
            } else {
                originalAction.actionPerformed(e);
            }
        }
    }

    /**
     * Action of <tt>Goto First</tt> key events in scrollable table.
     */
    private final class ScrollFirstAction extends AbstractAction {
        /**
         * the original action.
         */
        private final Action originalAction;

        /**
         * Contructor.
         *
         * @param theOriginalAction the original action
         */
        private ScrollFirstAction(final Action theOriginalAction) {
            super();
            this.originalAction = theOriginalAction;
        }

        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(final ActionEvent e) {
            if (e.getSource() == main && frozenColumns > 0 && main.getSelectedColumn() == 0) {
                main.transferFocusBackward();
                fixed.changeSelection(main.getSelectedRow(), 0, false, false);
            } else {
                originalAction.actionPerformed(e);
            }
        }
    }

    /**
     * Action of <tt>Goto Last</tt> key events in locked table.
     */
    private final class LockedLastAction extends AbstractAction {
        /**
         * the original action.
         */
        private final Action originalAction;

        /**
         * Contructor.
         *
         * @param theOriginalAction the original action
         */
        private LockedLastAction(final Action theOriginalAction) {
            super();
            this.originalAction = theOriginalAction;
        }

        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(final ActionEvent e) {
            if (e.getSource() == fixed && main.getColumnCount() > 0
                    && fixed.getSelectedColumn() == fixed.getColumnCount() - 1) {
                fixed.transferFocus();
                main.changeSelection(main.getSelectedRow(), main.getColumnCount() - 1, false, false);
            } else {
                originalAction.actionPerformed(e);
            }
        }
    }

    /**
     * Returns the number of the previous row in the table relative to the
     * current selection. If the selection is in the first row, the next row
     * is the first row.
     *
     * @param table a JTable for row calculation
     * @return row number of previous row
     */
    private int previousRow(final JTable table) {
        int row = table.getSelectedRow() - 1;
        if (row == -1) {
            row = table.getRowCount() - 1;
        }
        return row;
    }

    /**
     * /**
     * Returns the number of the next row in the table relative to the
     * current selection. If the selection is in the last row, the next row
     * is the first row.
     *
     * @param table a JTable for row calculation
     * @return row number of next row
     */
    protected int nextRow(final JTable table) {
        int row = table.getSelectedRow() + 1;
        if (row == table.getRowCount()) {
            row = 0;
        }
        return row;
    }
}