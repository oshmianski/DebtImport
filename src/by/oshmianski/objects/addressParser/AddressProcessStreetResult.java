package by.oshmianski.objects.addressParser;

import java.util.ArrayList;

/**
 * Created by vintselovich on 28.01.14.
 */
public class AddressProcessStreetResult {
    boolean find;
    String street;
    ArrayList<AddressParserItem> items;

    public AddressProcessStreetResult() {
        items = new ArrayList<AddressParserItem>();
    }

    public boolean isFind() {
        return find;
    }

    public void setFind(boolean find) {
        this.find = find;
    }

    public ArrayList<AddressParserItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<AddressParserItem> items) {
        this.items = items;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void processAllItems(AddressParserOperation operation){
        for(AddressParserItem item : items){
            item.setProcessed(true, operation);
            item.setTypeValue(AddressParserItemTypeValue.street);
        }
    }
}
