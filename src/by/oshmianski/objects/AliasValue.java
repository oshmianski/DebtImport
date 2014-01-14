package by.oshmianski.objects;

/**
 * Created by vintselovich on 27.12.13.
 */
public class AliasValue {
    private String alias;
    private String value;

    public AliasValue(String alias, String value) {
        this.alias = alias;
        this.value = value;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
