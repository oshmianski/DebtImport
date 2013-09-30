package by.oshmianski.objects;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: VintsalovichS
 * Date: 30.09.13
 * Time: 12:48
 */
public class RecordObject {
    private String title;

    private ArrayList<RecordObjectField> fields;

    public RecordObject(String title) {
        this.title = title;
        fields = new ArrayList<RecordObjectField>();
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<RecordObjectField> getFields() {
        return fields;
    }

    public void setFields(ArrayList<RecordObjectField> fields) {
        this.fields = fields;
    }

    public void addField(RecordObjectField field) {
        fields.add(field);
    }

    @Override
    public String toString() {
        return title;
    }
}
