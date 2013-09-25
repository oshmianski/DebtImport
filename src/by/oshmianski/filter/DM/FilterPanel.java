package by.oshmianski.filter.DM;

import by.oshmianski.filter.FilterComponent;
import by.oshmianski.ui.utils.RoundedPanel;
import by.oshmianski.main.AppletWindow;
import by.oshmianski.objects.DataMainItem;
import by.oshmianski.utils.MyLog;
import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FunctionList;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;
import ca.odell.glazedlists.matchers.CompositeMatcherEditor;
import ca.odell.glazedlists.matchers.MatcherEditor;
import ca.odell.glazedlists.swing.DefaultEventComboBoxModel;
import ca.odell.glazedlists.swing.DefaultEventTableModel;
import ca.odell.glazedlists.swing.JEventListPanel;
import com.jgoodies.forms.layout.RowSpec;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Created with IntelliJ IDEA.
 * User: 8-058
 * Date: 08.06.12
 * Time: 8:17
 */
public class FilterPanel {
    /**
     * the currently applied filters
     */
    private final EventList<CloseableFilterComponent> selectedFilterComponents = new BasicEventList();
    private final EventList<CloseableFilterComponent> remainingFilterComponents = new BasicEventList();

    private CompositeMatcherEditor<DataMainItem> matcherEditor;

    private JScrollPane filtersScrollPane;

    private MatcherEditorDMStatus matcherEditorDMStatus;
    private TextFilterComponent textFilterComponent;

    private EventList<MatcherEditor<DataMainItem>> matcherEditors;

    private EventList<DataMainItem> items;

    private boolean visibleControl;

    private DefaultEventTableModel model;

    public FilterPanel(EventList<DataMainItem> items, boolean visibleControl) {
        this.items = items;
        this.matcherEditors = new FunctionList(selectedFilterComponents, new CloseableFilterComponentToMatcherEditor<DataMainItem>());
        this.matcherEditor = new CompositeMatcherEditor(matcherEditors);
        this.visibleControl = visibleControl;
    }

    public void install(DefaultEventTableModel model) {
        // select some initial filters
        this.model = model;

        matcherEditorDMStatus = new MatcherEditorDMStatus(items, model, 150, visibleControl);
        textFilterComponent = new TextFilterComponent();

        this.selectedFilterComponents.add(new CloseableFilterComponent(model, matcherEditorDMStatus, selectedFilterComponents, remainingFilterComponents, visibleControl));
        // and then have the rest

        this.remainingFilterComponents.add(new CloseableFilterComponent(model, textFilterComponent, selectedFilterComponents, remainingFilterComponents, visibleControl));

        // create the filters panel
        JPanel filtersPanel = new JEventListPanel(selectedFilterComponents, new CloseableFilterComponentPanelFormat<DataMainItem>());
        filtersPanel.setOpaque(true);
        filtersPanel.setBackground(AppletWindow.REPORT_PANEL_BACKGROUND);
//        filtersPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
//        filtersPanel.setBorder(BorderFactory.createLineBorder(Color.red));

        // create a wrapped panel that adds the 'add' button
        JPanel filtersPanelPlusAddButton = new JPanel(new BorderLayout());
        filtersPanelPlusAddButton.add(filtersPanel, BorderLayout.CENTER);
        if (visibleControl)
            filtersPanelPlusAddButton.add(new AddFilterControl(model, selectedFilterComponents, remainingFilterComponents).getComponent(), BorderLayout.SOUTH);

        filtersPanelPlusAddButton.setBackground(Color.WHITE);

        filtersScrollPane = new JScrollPane(filtersPanelPlusAddButton);
        filtersScrollPane.setPreferredSize(new Dimension(240, 10));
        filtersScrollPane.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createEmptyBorder(2, 0, 2, 4),
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(new Color(0x9297A1)),
                                BorderFactory.createEmptyBorder(2, 2, 2, 2)
                        )
                )
        );
    }

    public void reinstallModel(DefaultEventTableModel model) {
        matcherEditorDMStatus.setModel(model);
    }

    public CompositeMatcherEditor<DataMainItem> getMatcherEditor() {
        return matcherEditor;
    }

    public JComponent getComponent() {
        return filtersScrollPane;
    }

    /**
     * A cute little panel with a kill button to remove this filter.
     */
    private static class CloseableFilterComponent implements ActionListener {
        private DefaultEventTableModel model;
        private FilterComponent filterComponent;
        private JButton closeButton;
        private JLabel headerLabel;
        private JPanel headerPanel;
        private JPanel wrapPanel;

        private EventList<CloseableFilterComponent> selectedFilterComponents = new BasicEventList();
        private EventList<CloseableFilterComponent> remainingFilterComponents = new BasicEventList();

        public CloseableFilterComponent(
                DefaultEventTableModel model,
                FilterComponent filterComponent,
                EventList<CloseableFilterComponent> selectedFilterComponents,
                EventList<CloseableFilterComponent> remainingFilterComponents,
                boolean visibleControl) {
            this.model = model;
            this.selectedFilterComponents = selectedFilterComponents;
            this.remainingFilterComponents = remainingFilterComponents;
            this.closeButton = new JButton();
            this.closeButton.addActionListener(this);
            this.closeButton.setOpaque(false);
            this.closeButton.setBorder(AppletWindow.EMPTY_ONE_PIXEL_BORDER);
            this.closeButton.setIcon(AppletWindow.X_ICON);
            this.closeButton.setContentAreaFilled(false);

            this.headerLabel = new JLabel();
            this.headerLabel.setHorizontalAlignment(JLabel.CENTER);
            this.headerLabel.setFont(headerLabel.getFont().deriveFont(10.0f));
            this.headerLabel.setForeground(AppletWindow.GLAZED_LISTS_DARK_BROWN);

            this.headerPanel = new JPanel();
            this.headerPanel.setOpaque(false);
            this.headerPanel.setLayout(new BorderLayout());
            this.headerPanel.add(headerLabel, BorderLayout.CENTER);
            if (visibleControl)
                this.headerPanel.add(closeButton, BorderLayout.EAST);
            if (filterComponent.isSelectDeselectAllVisible()) {
                JPanel seldeselPanel = new JPanel(new BorderLayout());
                seldeselPanel.setOpaque(false);
                JButton sel = new JButton();
                sel.addActionListener(filterComponent.getSelectAction());
                sel.setOpaque(false);
                sel.setBorder(AppletWindow.EMPTY_ONE_PIXEL_BORDER);
                sel.setIcon(AppletWindow.PLUS_ICON);
                sel.setContentAreaFilled(false);
                seldeselPanel.add(sel, BorderLayout.WEST);

                JButton desel = new JButton();
                desel.addActionListener(filterComponent.getDeselectAction());
                desel.setOpaque(false);
                desel.setBorder(AppletWindow.EMPTY_ONE_PIXEL_BORDER);
                desel.setIcon(AppletWindow.MINUS_ICON);
                desel.setContentAreaFilled(false);
                seldeselPanel.add(desel, BorderLayout.CENTER);

                this.headerPanel.add(seldeselPanel, BorderLayout.WEST);
            }

            this.wrapPanel = new RoundedPanel(AppletWindow.GLAZED_LISTS_LIGHT_BROWN);

            setFilterComponent(filterComponent);
        }

        public void actionPerformed(ActionEvent actionEvent) {
            if (actionEvent.getSource() == closeButton) {
                selectedFilterComponents.remove(this);
                remainingFilterComponents.add(this);

                if (model != null) {
                    model.fireTableDataChanged();
                } else {
                    System.out.println("model FP = null");
                }

            } else {
                throw new IllegalStateException();
            }
        }

        public void setFilterComponent(FilterComponent filterComponent) {
            this.filterComponent = filterComponent;
            this.headerLabel.setText(filterComponent.toString());
        }

        public MatcherEditor getMatcherEditor() {
            return filterComponent.getMatcherEditor();
        }

        public JComponent getHeader() {
            return headerPanel;
        }

        public JComponent getComponent() {
            return filterComponent.getComponent();
        }

        public JPanel getWrapPanel() {
            return wrapPanel;
        }

        public String toString() {
            return filterComponent.toString();
        }
    }

    /**
     * A combobox to add a new filter.
     */
    private static class AddFilterControl implements ItemListener, ListEventListener {
        private DefaultEventTableModel model;
        private JComboBox filterSelect;
        private JLabel selectLabel;
        private JComponent panel;

        private EventList<CloseableFilterComponent> selectedFilterComponents = new BasicEventList<CloseableFilterComponent>();
        private EventList<CloseableFilterComponent> remainingFilterComponents = new BasicEventList<CloseableFilterComponent>();

        public AddFilterControl(
                DefaultEventTableModel model,
                EventList<CloseableFilterComponent> selectedFilterComponents,
                EventList<CloseableFilterComponent> remainingFilterComponents) {
            this.model = model;
            this.selectedFilterComponents = selectedFilterComponents;
            this.remainingFilterComponents = remainingFilterComponents;

            filterSelect = new JComboBox(new DefaultEventComboBoxModel(remainingFilterComponents));
            filterSelect.setFont(filterSelect.getFont().deriveFont(10.0f));
            filterSelect.setOpaque(false);

            selectLabel = new JLabel("Фильтры: ");
            selectLabel.setForeground(Color.BLACK);
            selectLabel.setFont(selectLabel.getFont().deriveFont(10.0f));
            selectLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

            panel = new JPanel();
            panel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
//            panel.setBackground(report.GLAZED_LISTS_MEDIUM_LIGHT_BROWN);
            panel.setBackground(AppletWindow.REPORT_PANEL_BACKGROUND);
            panel.setLayout(new GridBagLayout());
            panel.add(selectLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
            panel.add(filterSelect, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
            panel.add(Box.createVerticalGlue(), new GridBagConstraints(0, 1, 2, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

            // handle selections of a filter
            filterSelect.addItemListener(this);
            // disable the combobox when there aren't any options
            remainingFilterComponents.addListEventListener(this);

            filterSelect.setEnabled(remainingFilterComponents.size() != 0);
        }

        /**
         * Get the filter control panel.
         */
        public JComponent getComponent() {
            return panel;
        }

        /**
         * {@inheritDoc}
         */
        public void listChanged(ListEvent listChanges) {
            boolean enabled = !remainingFilterComponents.isEmpty();
            filterSelect.setEnabled(enabled);
            selectLabel.setForeground(enabled ? Color.BLACK : AppletWindow.GLAZED_LISTS_MEDIUM_BROWN);

            if (model != null) {
                model.fireTableDataChanged();
            } else {
                System.out.println("model listChanged = null");
            }
        }

        /**
         * {@inheritDoc}
         */
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() != ItemEvent.SELECTED) return;
            CloseableFilterComponent selectedFilterComponent = (CloseableFilterComponent) filterSelect.getSelectedItem();
            if (selectedFilterComponent == null) return;

            selectedFilterComponents.add(selectedFilterComponent);
            filterSelect.setSelectedItem(null);
            remainingFilterComponents.remove(selectedFilterComponent);

            if (model != null) {
                model.fireTableDataChanged();
            } else {
                System.out.println("model itemStateChanged = null");
            }
        }
    }

    /**
     * Provide layout for {@link CloseableFilterComponent}s.
     */
    private static class CloseableFilterComponentPanelFormat<E> extends JEventListPanel.AbstractFormat<CloseableFilterComponent> {
        protected CloseableFilterComponentPanelFormat() {
            setElementCells("4px, min, 3px, pref, 4px", "4px, fill:pref:grow, 4px");
//            setGaps("4px", "0px");
            setGapRow(RowSpec.decode("4px"));
            setGapColumn(null);
            setCellConstraints(new String[]{"2, 2", "2, 4", "1, 1, 3, 5"});
        }

        public JComponent getComponent(CloseableFilterComponent element, int component) {
            if (component == 0) return element.getHeader();
            else if (component == 1) return element.getComponent();
            else if (component == 2) return element.getWrapPanel();
            else throw new IllegalStateException();
        }
    }

    /**
     * Convert a list of {@link CloseableFilterComponentToMatcherEditor}s into
     * {@link ca.odell.glazedlists.matchers.MatcherEditor}s.
     */
    private static class CloseableFilterComponentToMatcherEditor<E> implements FunctionList.Function<CloseableFilterComponent, MatcherEditor<E>> {
        public MatcherEditor<E> evaluate(CloseableFilterComponent sourceValue) {
            return sourceValue.getMatcherEditor();
        }
    }

    public void dispose() {
        try {
            selectedFilterComponents.dispose();
            remainingFilterComponents.dispose();

            if (matcherEditorDMStatus != null) matcherEditorDMStatus.dispose();
            if (textFilterComponent != null) textFilterComponent.dispose();

            matcherEditors.clear();
        } catch (Exception e) {
            MyLog.add2Log(e);
        }
    }

    public MatcherEditorDMStatus getMatcherEditorDMStatus() {
        return matcherEditorDMStatus;
    }

    public TextFilterComponent getTextFilterComponent() {
        return textFilterComponent;
    }
}
