package by.oshmianski.objects.addressParser;

/**
 * Created by vintselovich on 11.01.14.
 */
public class AddressItem {
    private String title;
    private String value;
    private AddressParserOperation operation;

    public AddressItem(String title, String value, AddressParserOperation operation) {
        this.title = title;
        this.value = value;
        this.operation = operation;
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

    public AddressParserOperation getOperation() {
        return operation;
    }

    public void setOperation(AddressParserOperation operation) {
        this.operation = operation;
    }
}
