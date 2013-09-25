package by.oshmianski.filter;

import ca.odell.glazedlists.matchers.MatcherEditor;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * Created with IntelliJ IDEA.
 * User: 8-058
 * Date: 07.06.12
 * Time: 9:05
 */
public interface FilterComponent<E> {

    public JComponent getComponent();

    public boolean isSelectDeselectAllVisible();

    public ActionListener getSelectAction();
    public ActionListener getDeselectAction();

    public MatcherEditor<E> getMatcherEditor();

    public void dispose();
}
