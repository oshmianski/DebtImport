package by.oshmianski.objects.addressParser;

import java.util.ArrayList;

/**
 * Created by vintselovich on 28.01.14.
 */
public class AddressProcessCityResult {
    boolean find;
    String city;
    String cityType;
    ArrayList<AddressParserItem> items;

    public AddressProcessCityResult() {
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityType() {
        return cityType;
    }

    public void setCityType(String cityType) {
        this.cityType = cityType;
    }

    public void processAllItems(AddressParserItemTypeValue typeValue){
        for(AddressParserItem item : items){
            item.setProcessed(true);
            item.setTypeValue(typeValue);
        }
    }
}
