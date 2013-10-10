package by.oshmianski.objects;

import ca.odell.glazedlists.impl.sort.ReverseComparator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created with IntelliJ IDEA.
 * User: VintsalovichS
 * Date: 30.09.13
 * Time: 12:48
 */
public class RecordObject implements Comparable<RecordObject>{
    private int number;
    private String title;
    private boolean flagEmpty;
    private boolean existInDB;
    private boolean existInPrevios;
    private String linkKey;
    private RecordObject mainObject;

    private ArrayList<RecordObjectField> fields;

    public RecordObject(int number, String title) {
        this.number = number;
        this.title = title;
        fields = new ArrayList<RecordObjectField>();
        flagEmpty = false;
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
        Collections.sort(fields, new Comparator<RecordObjectField>() {
            @Override
            public int compare(RecordObjectField o1, RecordObjectField o2) {
                return o1.getTitle().toLowerCase().compareTo(o2.getTitle().toLowerCase());
            }
        });
        this.fields = fields;
    }

    public void addField(RecordObjectField field) {
        fields.add(field);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isFlagEmpty() {
        return flagEmpty;
    }

    public void setFlagEmpty(boolean flagEmpty) {
        this.flagEmpty = flagEmpty;
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

    public String getLinkKey() {
        return linkKey;
    }

    public void setLinkKey(String linkKey) {
        this.linkKey = linkKey;
    }

    public RecordObject getMainObject() {
        return mainObject;
    }

    public void setMainObject(RecordObject mainObject) {
        this.mainObject = mainObject;
    }

    public int getNumber() {
        return number;
    }

    public RecordObjectField getFieldByTitle(String title) {
        for (RecordObjectField recordObjectField : fields) {
            if (recordObjectField.getTitle().equals(title)) return recordObjectField;
        }

        return null;
    }

    @Override
    public String toString() {
        return title;
    }

    @Override
    public int compareTo(RecordObject o) {
        return o.getTitle().compareTo(getTitle());
    }
}
