package by.oshmianski.filter.DM;

import by.oshmianski.filter.FilterComponent;
import by.oshmianski.objects.DataMainItem;
import by.oshmianski.objects.Status;
import by.oshmianski.ui.utils.niceScrollPane.NiceScrollPane;
import by.oshmianski.utils.MyLog;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GroupingList;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;
import ca.odell.glazedlists.matchers.AbstractMatcherEditor;
import ca.odell.glazedlists.matchers.Matcher;
import ca.odell.glazedlists.matchers.MatcherEditor;
import ca.odell.glazedlists.swing.DefaultEventTableModel;
import ca.odell.glazedlists.swing.GlazedListsSwing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;
import java.util.*;
import java.util.List;

/**
 * A MatcherEditor that produces Matchers that filter the issues based on the
 * selected statuses.
 */
public class MatcherEditorDMStatuses extends AbstractMatcherEditor implements ListEventListener, ActionListener, FilterComponent {
    /**
     * A MessageFormat to generate pretty names for our CheckBoxes which include the number of bugs with that status.
     */
    private static final MessageFormat checkboxFormat = new MessageFormat("{0} {1,choice,0#|0<({1})}");

    /**
     * A panel housing a checkbox for each status.
     */
//    private JPanel checkBoxPanel = new JPanel(new GridLayout(4, 2));
    private JPanel checkBoxPanelGlobal = new JPanel();
    private JPanel checkBoxPanel = new JPanel();
    private NiceScrollPane filtersScrollPane = new NiceScrollPane(checkBoxPanel);

    /**
     * A checkbox for each displayed status.
     */
    private final Map statusCheckBoxes = new LinkedHashMap();

    /**
     * Issues grouped together by status.
     */
    private final GroupingList issuesByStatus;
    private final EventList issuesByStatusSwingThread;

    /**
     * A cache of the list of statuses that mirrors the statuses of the issuesByStatus List.
     * It is used to determine which status is deleted when DELETE events arrive.
     */
    private List statuses = new ArrayList();
    private ArrayList<Status> statusesArray = new ArrayList();
    private DefaultEventTableModel model;

    private int heaght;
    private boolean visibleControl;

    public MatcherEditorDMStatuses(EventList issues, DefaultEventTableModel model, int heaght, boolean visibleControl) {
        super();
        this.model = model;
        this.heaght = heaght;
        this.visibleControl = visibleControl;

        issuesByStatus = new GroupingList(issues, new ComparatorDMStatuses());
        this.issuesByStatusSwingThread = GlazedListsSwing.swingThreadProxyList(issuesByStatus);
        this.issuesByStatusSwingThread.addListEventListener(this);

        this.checkBoxPanel.setLayout(new BoxLayout(this.checkBoxPanel, BoxLayout.Y_AXIS));
        this.checkBoxPanel.setOpaque(true);
        this.checkBoxPanel.setBackground(Color.WHITE);
        this.checkBoxPanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
//        this.checkBoxPanelGlobal.setBackground(Color.WHITE);
        this.checkBoxPanelGlobal.setLayout(new BorderLayout());
        this.checkBoxPanelGlobal.add(filtersScrollPane, BorderLayout.CENTER);
        this.checkBoxPanelGlobal.setOpaque(false);
        this.checkBoxPanelGlobal.setPreferredSize(new Dimension(200, this.heaght));

        this.filtersScrollPane.setOpaque(false);
        this.filtersScrollPane.setBackground(Color.WHITE);
    }

    public void setModel(DefaultEventTableModel model) {
        this.model = model;
    }

    /**
     * Returns the component responsible for editing the status filter.
     */
    public JComponent getComponent() {
        return this.checkBoxPanelGlobal;
    }

    public String toString() {
        return "Статус";
    }

    public MatcherEditor getMatcherEditor() {
        return this;
    }

    /**
     * A convenience method to build a status checkbox with the given name.
     */
    private static JCheckBox buildCheckBox(String name) {
        final JCheckBox checkBox = new JCheckBox(name, true);
        checkBox.setName(name);
        checkBox.setOpaque(false);
        checkBox.setFocusable(false);
        checkBox.setMargin(new Insets(0, 0, 0, 0));
        return checkBox;
    }

    /**
     * Returns a StatusMatcher which matches Issues if their status is one
     * of the selected statuses.
     */
    private StatusMatcher buildMatcher() {
        final Set allowedStates = new HashSet();
        for (Iterator iter = statusCheckBoxes.entrySet().iterator(); iter.hasNext(); ) {
            Map.Entry entry = (Map.Entry) iter.next();
            if (((JCheckBox) entry.getValue()).isSelected()) allowedStates.add(entry.getKey());
        }
        return new StatusMatcher(allowedStates);
    }

    public void listChanged(ListEvent listChanges) {
        boolean isDelete;
        String listChangeType = "";

        statusesArray.clear();
        while (listChanges.next()) {
            isDelete = false;
            final int type = listChanges.getType();
            final int index = listChanges.getIndex();
            Status status = null;
            int count = 0;
            if (type == ListEvent.INSERT) {
                List issuesOfThisStatus = (List) issuesByStatusSwingThread.get(index);
                DataMainItem dataMainItem = ((DataMainItem) issuesOfThisStatus.get(0));
                for (Status status1 : dataMainItem.getStatuses()) {
                    if (!statusesArray.contains(status1)) {
                        statusesArray.add(status1);
                        statuses.add(index, status1);
                    }
                }
                count = issuesOfThisStatus.size();
                listChangeType = "INSERT";
            } else if (type == ListEvent.UPDATE) {
                List issuesOfThisStatus = (List) issuesByStatusSwingThread.get(index);
                status = (Status) statuses.get(index);
                count = issuesOfThisStatus.size();
                statusesArray.add(status);
                listChangeType = "UPDATE";
            } else if (type == ListEvent.DELETE) {
                status = (Status) statuses.remove(index);
                statusesArray.add(status);
                count = 0;

                checkBoxPanel.remove((JCheckBox) statusCheckBoxes.get(status));
                checkBoxPanel.revalidate();
                checkBoxPanel.repaint();
                statusCheckBoxes.remove(status);

                isDelete = true;
                listChangeType = "DELETE";
            } else {
                throw new IllegalStateException();
            }

            if (!isDelete) {
                for (Status status1 : statusesArray) {
                    final JCheckBox checkBox = (JCheckBox) statusCheckBoxes.get(status1);

                    if (checkBox != null) {
                        checkBox.setText(checkboxFormat.format(new Object[]{checkBox.getName(), new Integer(count)}));
                        System.out.println("listChangeType=" + listChangeType + ", checkBox.getName()=" + checkBox.getName() + ", count=" + count);
                    } else {
                        JCheckBox checkBox1 = buildCheckBox(String.valueOf(status1));
                        statusCheckBoxes.put(status1, checkBox1);
                        checkBox1.addActionListener(this);
                        checkBox1.setText(checkboxFormat.format(new Object[]{status1, new Integer(count)}));
                        checkBoxPanel.add(checkBox1);
                    }
                }
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        final boolean isCheckBoxSelected = ((JCheckBox) (JCheckBox) e.getSource()).isSelected();
        final StatusMatcher statusMatcher = this.buildMatcher();
        if (statusMatcher.getStateCount() == 0) this.fireMatchNone();
        else if (statusMatcher.getStateCount() == this.statusCheckBoxes.size()) this.fireMatchAll();
        else if (isCheckBoxSelected) this.fireRelaxed(statusMatcher);
        else this.fireConstrained(statusMatcher);

        if (model != null) {
            model.fireTableDataChanged();
        } else {
            System.out.println("model = null");
        }
    }

    /**
     * A StatusMatcher returns <tt>true</tt> if the status of the Issue is
     * one of the viewable status selected by the user.
     */
    private static class StatusMatcher implements Matcher {
        private final Set allowedStatuses;

        public StatusMatcher(Set allowedStatuses) {
            super();
            this.allowedStatuses = allowedStatuses;
        }

        public int getStateCount() {
            return this.allowedStatuses.size();
        }

        public boolean matches(Object x0) {
            DataMainItem issue = (DataMainItem) x0;
            for (Status status : issue.getStatuses()) {
                if (this.allowedStatuses.contains(status)) return true;
            }
            return false;
        }
        /*missing*/
    }

    @Override
    public boolean isSelectDeselectAllVisible() {
        return true;
    }

    @Override
    public ActionListener getSelectAction() {
        return new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                for (Object key : statusCheckBoxes.keySet()) {
                    JCheckBox cb = (JCheckBox) statusCheckBoxes.get(key);
                    cb.setSelected(true);
                }

                final StatusMatcher statusMatcher = buildMatcher();
                fireMatchAll();

                if (model != null) {
                    model.fireTableDataChanged();
                } else {
                    System.out.println("model = null");
                }
            }
        };
    }

    @Override
    public ActionListener getDeselectAction() {
        return new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                for (Object key : statusCheckBoxes.keySet()) {
                    JCheckBox cb = (JCheckBox) statusCheckBoxes.get(key);
                    cb.setSelected(false);
                }

                final StatusMatcher statusMatcher = buildMatcher();
                fireMatchNone();

                if (model != null) {
                    model.fireTableDataChanged();
                } else {
                    System.out.println("model = null");
                }
            }
        };
    }

    @Override
    public void dispose() {
        try {
            statusCheckBoxes.clear();
            statuses.clear();
//            issuesByStatus.dispose();
//            issuesByStatusSwingThread.dispose();
//            if (model != null) model.dispose();
        } catch (Exception e) {
            MyLog.add2Log(e);
        }
    }

    public void fireMatchAllA() {
        fireMatchAll();
    }
}
