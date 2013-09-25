package by.oshmianski.filter.DM;

import by.oshmianski.filter.FilterComponent;
import by.oshmianski.objects.DataMainItem;
import ca.odell.glazedlists.TextFilterator;
import ca.odell.glazedlists.matchers.AbstractMatcherEditor;
import ca.odell.glazedlists.matchers.MatcherEditor;
import ca.odell.glazedlists.swing.TextComponentMatcherEditor;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * @author <a href="mailto:jesse@swank.ca">Jesse Wilson</a>
 */
public class TextFilterComponent extends AbstractMatcherEditor implements FilterComponent<DataMainItem> {

    private static final TextFilterator<DataMainItem> ISSUE_TEXT_FILTERATOR = new FilteratorDataMainItem();

    private JTextField filterEdit = new JTextField(15);
    private TextComponentMatcherEditor<DataMainItem> textComponentMatcherEditor = new TextComponentMatcherEditor<DataMainItem>(filterEdit, ISSUE_TEXT_FILTERATOR, true);

    public JComponent getComponent() {
        return filterEdit;
    }

    @Override
    public boolean isSelectDeselectAllVisible() {
        return false;
    }

    @Override
    public ActionListener getSelectAction() {
        return null;
    }

    @Override
    public ActionListener getDeselectAction() {
        return null;
    }

    @Override
    public MatcherEditor<DataMainItem> getMatcherEditor() {
        return textComponentMatcherEditor;
    }

    @Override
    public void dispose() {

    }

    public String toString() {
        return "Описание";
    }

    public void fireMatchAllA() {
//        filterEdit.notify();
    }
}