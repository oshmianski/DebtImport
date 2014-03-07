package by.oshmianski.objects.addressParser;

import java.util.ArrayList;

/**
 * Created by vintselovich on 28.01.14.
 */
public class AddressProcessCityWithGEOResult {
    boolean find;
    String city;
    String cityType;

    public AddressProcessCityWithGEOResult() {
    }

    public boolean isFind() {
        return find;
    }

    public void setFind(boolean find) {
        this.find = find;
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
}
