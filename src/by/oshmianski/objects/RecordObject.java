package by.oshmianski.objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created with IntelliJ IDEA.
 * User: VintsalovichS
 * Date: 30.09.13
 * Time: 12:48
 */
public class RecordObject implements Comparable<RecordObject> {
    private String objUnid;
    private int number;
    private String unidTitle;
    private String title;
    private String titleUser;
    private boolean flagEmpty;
    private boolean existInDB;
    private boolean existInPrevios;
    private boolean computeWithForm;
    private String linkKey;
    private String db;
    private ArrayList<RecordObject> mainObjects;

    private ArrayList<RecordObjectField> fields;

    public RecordObject(
            String objUnid,
            int number,
            String unidTitle,
            String title,
            String titleUser,
            String db,
            boolean computeWithForm) {
        this.objUnid = objUnid;
        this.number = number;
        this.unidTitle = unidTitle;
        this.title = title;
        this.titleUser = titleUser;
        this.db = db;
        this.computeWithForm = computeWithForm;

        fields = new ArrayList<RecordObjectField>();
        mainObjects = new ArrayList<RecordObject>();
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

    public ArrayList<RecordObject> getMainObject() {
        return mainObjects;
    }

    public void addMainObject(RecordObject mainObject) {
        mainObjects.add(mainObject);
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

    public void setNumber(int number) {
        this.number = number;
    }

    public String getTitleUser() {
        return titleUser;
    }

    public void setTitleUser(String titleUser) {
        this.titleUser = titleUser;
    }

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public String getObjUnid() {
        return objUnid;
    }

    public void setObjUnid(String objUnid) {
        this.objUnid = objUnid;
    }

    public boolean isComputeWithForm() {
        return computeWithForm;
    }

    public void setComputeWithForm(boolean computeWithForm) {
        this.computeWithForm = computeWithForm;
    }

    public String getUnidTitle() {
        return unidTitle;
    }

    public void setUnidTitle(String unidTitle) {
        this.unidTitle = unidTitle;
    }

    @Override
    public String toString() {
        return title + " | " + titleUser;
    }

    @Override
    public int compareTo(RecordObject o) {
        return o.getTitle().compareTo(getTitle());
    }
}
