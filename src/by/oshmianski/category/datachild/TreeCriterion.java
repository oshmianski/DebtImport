package by.oshmianski.category.datachild;

import by.oshmianski.objects.DataChildItem;
import by.oshmianski.utils.MyLog;
import org.apache.log4j.Logger;

import javax.swing.event.SwingPropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public abstract class TreeCriterion implements Serializable {
    /**
     * The List of all possible Main.TreeCriterion objects.
     */
    private final SwingPropertyChangeSupport support = new SwingPropertyChangeSupport(this);

    /**
     * A human readable name used to identify this Main.TreeCriterion.
     */
    private final String name;

    private final String method;

    /**
     * A flag to indicate whether this Main.TreeCriterion is active (and thus participating in the TreeFormat) or not.
     */
    private boolean active;

    /**
     * A map from the title of each synthetic Main.Item to the actual Main.Item in the
     * hierarchy. This acts as a cache to prevent building redundant synthetic
     * hierarchy Items.
     */
    private static Map syntheticItemCache = new HashMap();

    public TreeCriterion(String name, String method, boolean active) {
        super();
        this.name = name;
        this.active = active;
        this.method = method;
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        support.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        support.removePropertyChangeListener(l);
    }

    /**
     * Returns a human readable name used to identify this Main.TreeCriterion.
     */
    public String getName() {
        return name;
    }

    public String getMethod() {
        return method;
    }

    /**
     * Returns <tt>true</tt> if this Main.TreeCriterion is actively participating in
     * the TreeFormat; <tt>false</tt> otherwise.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Activates or deactivates this Main.TreeCriterion from participating in the
     * TreeFormat that governs what the tree hierarchies will resemble.
     */
    public void setActive(boolean active) {
        if (this.active == active) return;
        this.active = active;
        support.firePropertyChange("active", !active, active);
    }

    /**
     * Implementations of this method should produce a new Main.Item object whose
     * title reflects some aspect of the given <code>item</code> and is
     * appropriate for use as a tree hierarchy node value.
     */
    public abstract DataChildItem getPathItem(DataChildItem item, String parentCat);

    /**
     * A convenience method to lazily populate the {@link #syntheticItemCache}
     * and return a new Main.Item with the given <code>title</code>.
     */
    DataChildItem getOrCreateItem(DataChildItem item, String title, String parentCat) {
        DataChildItem pathItem = (DataChildItem) syntheticItemCache.get(parentCat + title);
        if (pathItem == null) {
            pathItem = createSyntheticItem(item, title);
            syntheticItemCache.put(parentCat + title, pathItem);
        }
        return pathItem;
    }

    /**
     * A convenience method to build and return a synthetic Main.Item whose title is
     * the given <code>string</code>.
     */
    private static DataChildItem createSyntheticItem(DataChildItem item, String string) {
        final DataChildItem itemNew = new DataChildItem();
        itemNew.setSynthetic(true);
        itemNew.setMainCat(string);
        return itemNew;
    }

    public final void dispose() {

        try {
            syntheticItemCache.clear();
            syntheticItemCache = null;
        } catch (Exception e) {
            MyLog.add2Log(e);
        }
    }
}
