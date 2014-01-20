package by.oshmianski.objects.addressParser;

/**
 * Created by vintselovich on 18.01.14.
 */
public class AliasValueType {
    private String alias;
    private AddressParserItemTypeValue typeValue;
    private String typeValue2;

    public AliasValueType(String alias, AddressParserItemTypeValue typeValue, String typeValue2) {
        this.alias = alias;
        this.typeValue = typeValue;
        this.typeValue2 = typeValue2;
    }

    public String getAlias() {
        return alias;
    }

    public AddressParserItemTypeValue getTypeValue() {
        return typeValue;
    }

    public String getTypeValue2() {
        return typeValue2;
    }
}
