package by.oshmianski.objects.addressParser;

/**
 * Created by vintselovich on 11.01.14.
 */
public class AddressItem {
    private String title;
    private String value;

    public AddressItem(String title, String value) {
        this.title = title;
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
