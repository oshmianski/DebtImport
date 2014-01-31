package by.oshmianski.models;

import by.oshmianski.category.datachild.ItemCats;
import by.oshmianski.objects.DataChildItem;
import ca.odell.glazedlists.gui.TableFormat;
import ca.odell.glazedlists.gui.WritableTableFormat;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: VintsalovichS
 * Date: 9/16/13
 * Time: 3:23 PM
 */
public class DataChildModel implements WritableTableFormat<DataChildItem> {
    private String[] columnNames = {"Статус", "Объект \\ Значение", "Описание"};
    private Map<Integer, ItemCats> listCat;

    public DataChildModel(Map<Integer, ItemCats> listCat) {
        this.listCat = listCat;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int i) {
        return columnNames[i];
    }

    @Override
    public Object getColumnValue(DataChildItem item, int i) {
        ItemCats cats = null;

        if (item != null) {
            if (item.getParentCat() != null) {
                cats = listCat.get((item.getParentCat() + item.getMainCat()).hashCode());
            }
        }

        switch (i) {
            case 0:
                return item.isSynthetic() ? null : item.getStatus();
            case 1:
                return item;
            case 2:
                return item.isSynthetic() ? null : item.getDescription();
            default:
                return null;
        }
    }

    @Override
    public boolean isEditable(DataChildItem dataChildItem, int i) {
        return i == 2;
    }

    @Override
    public DataChildItem setColumnValue(DataChildItem dataChildItem, Object o, int i) {
        return null;
    }
}
