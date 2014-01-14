package by.oshmianski.models;

import by.oshmianski.category.datachild.ItemCats;
import by.oshmianski.objects.DataChildItem;
import by.oshmianski.objects.addressParser.AddressParserItem;
import ca.odell.glazedlists.gui.WritableTableFormat;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: VintsalovichS
 * Date: 9/16/13
 * Time: 3:23 PM
 */
public class AddressParserModel implements WritableTableFormat<AddressParserItem> {
    private String[] columnNames = {"Номер", "Текст", "После", "До", "Тип", "Значение", "Значение2", "Начинается с цифры", "Является индексом", "Разобрано?"};

    public AddressParserModel() {

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
    public Object getColumnValue(AddressParserItem item, int i) {
        switch (i) {
            case 0:
                return item.getNumber();
            case 1:
                return item.getText();
            case 2:
                return item.getCharAfter();
            case 3:
                return item.getCharBefore();
            case 4:
                return item.getType();
            case 5:
                return item.getTypeValue();
            case 6:
                return item.getTypeValue2();
            case 7:
                return item.isBeginWithNumber();
            case 8:
                return item.isIndex();
            case 9:
                return item.isProcessed();
            default:
                return null;
        }
    }

    @Override
    public boolean isEditable(AddressParserItem item, int i) {
        if (i == 2) return true;
        return false;
    }

    @Override
    public AddressParserItem setColumnValue(AddressParserItem item, Object o, int i) {
        return null;
    }
}
