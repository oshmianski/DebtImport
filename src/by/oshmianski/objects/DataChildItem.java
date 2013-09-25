package by.oshmianski.objects;

import by.oshmianski.category.Cat;

/**
 * Created with IntelliJ IDEA.
 * User: VintsalovichS
 * Date: 9/16/13
 * Time: 2:27 PM
 */
public class DataChildItem extends Cat implements Comparable<DataChildItem> {
    private Status status;
    private String object;
    private String value;
    private String description;

    public DataChildItem() {
        this(Status.OK, "", "", "");
    }

    public DataChildItem(Status status, String object, String value, String description) {
        this.status = status;
        this.object = object;
        this.value = value;
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return getObject();
    }

    @Override
    public int compareTo(DataChildItem o) {
        return getMainCat().compareTo(o.getMainCat());
    }
}
