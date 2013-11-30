package by.oshmianski.models;

import by.oshmianski.objects.DataMainItem;
import ca.odell.glazedlists.gui.TableFormat;
import ca.odell.glazedlists.gui.WritableTableFormat;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Array;
import java.util.HashSet;

/**
 * Created with IntelliJ IDEA.
 * User: VintsalovichS
 * Date: 9/16/13
 * Time: 11:34 AM
 */
public class DataMainModel implements WritableTableFormat<DataMainItem> {
    private String[] colsTitle = {"Строка", "Статус", "Описание"};

    @Override
    public int getColumnCount() {
        return colsTitle.length;
    }

    @Override
    public String getColumnName(int i) {
        return colsTitle[i];
    }

    @Override
    public Object getColumnValue(DataMainItem dataMainItem, int i) {
        switch (i) {
            case 0:
                return dataMainItem.getLineNum();
            case 1:
                return new HashSet(dataMainItem.getStatuses());
            case 2:
                return dataMainItem.getDescription();
            default:
                return null;
        }
    }


    @Override
    public boolean isEditable(DataMainItem dataMainItem, int i) {
        if(i == 2) return true;
        return false;
    }

    @Override
    public DataMainItem setColumnValue(DataMainItem dataMainItem, Object o, int i) {
        return null;
    }
}
