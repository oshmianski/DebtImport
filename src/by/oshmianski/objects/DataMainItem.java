package by.oshmianski.objects;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: VintsalovichS
 * Date: 9/16/13
 * Time: 11:30 AM
 */
public class DataMainItem {
    private long lineNum;
    private Status status;
    private String description;

//    private EventList<DataChildItem> dataChildItems = new BasicEventList<DataChildItem>();
//    private ArrayList<DataChildItem> dataChildItems = new ArrayList<DataChildItem>();
    private ArrayList<DataChildItem> dataChildItems;
    private ArrayList<RecordObject> objects;

    public DataMainItem(long lineNum, Status status, String description) {
        this.lineNum = lineNum;
        this.status = status;
        this.description = description;
    }

    public long getLineNum() {
        return lineNum;
    }

    public void setLineNum(long lineNum) {
        this.lineNum = lineNum;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<DataChildItem> getDataChildItems() {
        return dataChildItems;
    }

    public void setDataChildItems(ArrayList<DataChildItem> dataChildItems) {
        this.dataChildItems = dataChildItems;
    }

    public ArrayList<RecordObject> getObjects() {
        return objects;
    }

    public void setObjects(ArrayList<RecordObject> objects) {
        this.objects = objects;
    }
}
