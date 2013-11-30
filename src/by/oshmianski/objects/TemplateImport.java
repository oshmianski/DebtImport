package by.oshmianski.objects;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: VintsalovichS
 * Date: 9/18/13
 * Time: 1:52 PM
 */
public class TemplateImport {
    private String unid;
    private int num;
    private String title;
    private String description;
    private boolean createFI;
    private boolean attachFile;
    private String db;
    private String dbID;
    private ArrayList<Integer> excludeStatuses = new ArrayList<Integer>();

    private EventList<Object> objects = new BasicEventList<Object>();
    private EventList<Link> links = new BasicEventList<Link>();
    private Object importFact;

    public TemplateImport(
            String unid,
            int num,
            String title,
            String description,
            boolean createFI,
            boolean attachFile,
            String db,
            String dbID) {
        this.unid = unid;
        this.num = num;
        this.title = title;
        this.description = description;
        this.createFI = createFI;
        this.attachFile = attachFile;
        this.db = db;
        this.dbID = dbID;
    }

    public EventList<Object> getObjects() {
        return objects;
    }

    public EventList<Link> getLinks() {
        return links;
    }

    public String getUnid() {

        return unid;
    }

    public void setUnid(String unid) {
        this.unid = unid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCreateFI() {
        return createFI;
    }

    public void setCreateFI(boolean createFI) {
        this.createFI = createFI;
    }

    public boolean isAttachFile() {
        return attachFile;
    }

    public void setAttachFile(boolean attachFile) {
        this.attachFile = attachFile;
    }

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public String getDbID() {
        return dbID;
    }

    public void setDbID(String dbID) {
        this.dbID = dbID;
    }

    public void addObject(Object object) {
        objects.add(object);
    }

    public void addLink(Link link) {
        links.add(link);
    }

    public Link getLinkByChildTitle(String title) {
        if (links == null) return null;

        for (Link link : links) {
            if (title.equals(link.getChildObject().getFormName())) return link;
        }

        return null;
    }

    public Link getLinkByMainAndChildTitle(String mainTitle, String childTitle){
        if (links == null) return null;

        for (Link link : links) {
            if (mainTitle.equals(link.getMainObject().getFormName()) && childTitle.equals(link.getChildObject().getFormName())) return link;
        }

        return null;
    }

    public Object getObjectByFormName(String formName) {
        if (objects == null) return null;

        for (Object object : objects) {
            if (formName.equals(object.getFormName())) return object;
        }

        return null;
    }

    public Object getImportFact() {
        return importFact;
    }

    public void setImportFact(Object importFact) {
        this.importFact = importFact;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public void addExcludeStatus(Status status){
        excludeStatuses.add(status.statusOrdinal);
    }

    public ArrayList<Integer> getExcludeStatuses(){
        return excludeStatuses;
    }

    @Override
    public String toString() {
        return getTitle();
    }
}
