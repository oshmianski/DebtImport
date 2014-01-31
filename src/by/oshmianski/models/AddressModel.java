package by.oshmianski.models;

import by.oshmianski.objects.addressParser.AddressItem;
import by.oshmianski.objects.addressParser.AddressParserItem;
import ca.odell.glazedlists.gui.WritableTableFormat;

/**
 * Created with IntelliJ IDEA.
 * User: VintsalovichS
 * Date: 9/16/13
 * Time: 3:23 PM
 */
public class AddressModel implements WritableTableFormat<AddressItem> {
    private String[] columnNames = {"Название", "Значение", "Операция"};

    public AddressModel() {

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
    public Object getColumnValue(AddressItem item, int i) {
        switch (i) {
            case 0:
                return item.getTitle();
            case 1:
                return item.getValue();
            case 2:
                return  item.getOperation();
            default:
                return null;
        }
    }

    @Override
    public boolean isEditable(AddressItem item, int i) {
        return i == 1;
    }

    @Override
    public AddressItem setColumnValue(AddressItem item, Object o, int i) {
        return null;
    }
}
