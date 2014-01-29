package by.oshmianski.objects.addressParser;

/**
 * Created by vintselovich on 28.01.14.
 */
public class AddressProcessStreetWithGEOResult {
    boolean find;
    String street;

    public AddressProcessStreetWithGEOResult() {
    }

    public boolean isFind() {
        return find;
    }

    public void setFind(boolean find) {
        this.find = find;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }
}
