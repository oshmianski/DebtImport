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
    private String[] columnNames = {"Номер", "Текст", "Разобрано?", "После", "До", "Тип", "Значение", "Значение2", "Начинается с цифры", "Является индексом"};

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
                return item.isProcessed();
            case 3:
                return item.getCharAfter();
            case 4:
                return item.getCharBefore();
            case 5:
                return item.getType();
            case 6:
                return item.getTypeValue();
            case 7:
                return item.getTypeValue2();
            case 8:
                return item.isBeginWithNumber();
            case 9:
                return item.isIndex();
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
