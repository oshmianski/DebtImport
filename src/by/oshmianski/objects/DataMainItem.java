package by.oshmianski.objects;

import by.oshmianski.objects.addressParser.AddressParser;

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
    private int flag2color;

    private ArrayList<DataChildItem> dataChildItems;
    private ArrayList<RecordObject> objects;
    private TemplateImport ti;
    private AddressParser addressParser;

    public DataMainItem(long lineNum, Status status, String description, TemplateImport ti) {
        this.lineNum = lineNum;
        this.status = status;
        this.description = description;
        this.ti = ti;
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

    public Status getStatusFromChild() {
        Status status = Status.OK;

        if (dataChildItems == null) return Status.OK;

        for (DataChildItem dataChildItem : dataChildItems) {
            if (dataChildItem.getStatus().statusOrdinal < status.statusOrdinal) {
                status = dataChildItem.getStatus();
            }
        }

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

    public void addDataChildItem(DataChildItem dataChildItem) {
        dataChildItems.add(dataChildItem);
    }

    public ArrayList<RecordObject> getObjects() {
        return objects;
    }

    public void setObjects(ArrayList<RecordObject> objects) {
        this.objects = objects;
    }

    public RecordObject getRecordObjectByTitle(String title) {
        if (objects == null) return null;

        for (RecordObject recordObject : objects) {
            if (title.equals(recordObject.getTitle())) return recordObject;
        }

        return null;
    }

    public RecordObject getRecordObjectByObjUnid(String unid) {
        if (objects == null) return null;

        for (RecordObject recordObject : objects) {
            if (unid.equals(recordObject.getObjUnid())) return recordObject;
        }

        return null;
    }

    public ArrayList<Status> getStatuses() {
        ArrayList<Status> statuses = new ArrayList<Status>();

        for (DataChildItem dataChildItem : dataChildItems) {
                statuses.add(dataChildItem.getStatus());
        }

        return statuses;
    }

    public ArrayList<Status> getStatusesFilter() {
        ArrayList<Status> statuses = new ArrayList<Status>();
        ArrayList<Integer> statusesTI = ti.getExcludeStatuses();

        for (DataChildItem dataChildItem : dataChildItems) {
            Integer i = dataChildItem.getStatus().statusOrdinal;
            if (!statusesTI.contains(i))
                statuses.add(dataChildItem.getStatus());
        }

        return statuses;
    }

    public AddressParser getAddressParser() {
        return addressParser;
    }

    public void setAddressParser(AddressParser addressParser) {
        this.addressParser = addressParser;
    }

    public TemplateImport getTi() {
        return ti;
    }

    public void setTi(TemplateImport ti) {
        this.ti = ti;
    }

    public void clearData(){
        dataChildItems.clear();
        objects.clear();
        addressParser.getParserItems().clear();
        addressParser.setAddress(null);
    }

    public int getFlag2color() {
        return flag2color;
    }

    public void setFlag2color(int flag2color) {
        this.flag2color = flag2color;
    }
}
