package by.oshmianski.objects;

/**
 * Created by vintselovich on 15.10.2014.
 */
public class Replacement {
    private String value;
    private String replace;
    private boolean regex;

    public Replacement(String value, String replace, boolean regex) {
        this.value = value;
        this.replace = replace;
        this.regex = regex;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getReplace() {
        return replace;
    }

    public void setReplace(String replace) {
        this.replace = replace;
    }

    public boolean isRegex() {
        return regex;
    }

    public void setRegex(boolean regex) {
        this.regex = regex;
    }
}
