package by.oshmianski.objects;

import java.lang.Object;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: VintsalovichS
 * Date: 10.10.13
 * Time: 13:24
 */
public class CellField {
    private java.lang.Object value;
    private Field.TYPE type;
    private final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    public CellField(java.lang.Object value, Field.TYPE type) {
        this.value = value;
        this.type = type;
    }

    public Object getValue() {
        return value;
    }

    public String getValueAsString() {
        if(type == Field.TYPE.TEXT) return (String)value;
        if(type == Field.TYPE.NUMBER) return String.valueOf(value);
        if(type == Field.TYPE.DATETIME) return formatter.format((Date)value);

        return value.toString();
    }

    public Field.TYPE getType() {
        return type;
    }

    public void setValue(java.lang.Object value) {
        this.value = value;
    }

    public void setType(Field.TYPE type) {
        this.type = type;
    }
}
