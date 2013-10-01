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
    private boolean willBeCreated;
    private boolean existInDB;
    private boolean existInPrevios;

    private ArrayList<RecordObjectField> fields;

    public RecordObject(String title) {
        this.title = title;
        fields = new ArrayList<RecordObjectField>();
        willBeCreated = true;
        existInDB = false;
        existInPrevios = false;
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

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isWillBeCreated() {
        return willBeCreated;
    }

    public void setWillBeCreated(boolean willBeCreated) {
        this.willBeCreated = willBeCreated;
    }

    public boolean isExistInDB() {
        return existInDB;
    }

    public void setExistInDB(boolean existInDB) {
        this.existInDB = existInDB;
    }

    public boolean isExistInPrevios() {
        return existInPrevios;
    }

    public void setExistInPrevios(boolean existInPrevios) {
        this.existInPrevios = existInPrevios;
    }

    public RecordObjectField getFieldByTitle(String title){
        for(RecordObjectField recordObjectField : fields){
            if(recordObjectField.getTitle().equals(title)) return recordObjectField;
        }

        return null;
    }

    @Override
    public String toString() {
        return title;
    }
}
