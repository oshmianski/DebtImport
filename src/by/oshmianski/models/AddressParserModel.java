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
    private String[] columnNames = {"Номер", "Текст", "Разобрано?", "Разобрано без значения?", "После", "До", "Тип", "Значение", "Значение2", "Начинается с цифры", "Является индексом", "Операция"};

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
                return item.isProcessedWithoutValue();
            case 4:
                return item.getCharAfter();
            case 5:
                return item.getCharBefore();
            case 6:
                return item.getType();
            case 7:
                return item.getTypeValue();
            case 8:
                return item.getTypeValue2();
            case 9:
                return item.isBeginWithNumber();
            case 10:
                return item.isIndex();
            case 11:
                return item.getOperation();
            default:
                return null;
        }
    }

    @Override
    public boolean isEditable(AddressParserItem item, int i) {
        if (i == 1) return true;
        return false;
    }

    @Override
    public AddressParserItem setColumnValue(AddressParserItem item, Object o, int i) {
        return null;
    }
}
