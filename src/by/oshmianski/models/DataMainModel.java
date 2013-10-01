package by.oshmianski.models;

import by.oshmianski.objects.DataMainItem;
import ca.odell.glazedlists.gui.TableFormat;

/**
 * Created with IntelliJ IDEA.
 * User: VintsalovichS
 * Date: 9/16/13
 * Time: 11:34 AM
 */
public class DataMainModel implements TableFormat<DataMainItem> {
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
                return dataMainItem.getStatusFromChild();
            case 2:
                return dataMainItem.getDescription();
            default:
                return null;
        }
    }
}
