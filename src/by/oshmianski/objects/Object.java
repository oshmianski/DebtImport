package by.oshmianski.objects;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;

/**
 * Created with IntelliJ IDEA.
 * User: VintsalovichS
 * Date: 9/18/13
 * Time: 1:50 PM
 */
public class Object {
    private String unid;
    private int number;
    private String title;
    private String description;
    private String formName;
    private String dbUser;
    private String db;

    private EventList<Field> fields;
    private EventList<Key> keys;

    public Object(String unid, int number, String title, String description, String formName, String dbUser, String db) {
        this.number = number;
        this.unid = unid;
        this.title = title;
        this.description = description;
        this.formName = formName;
        this.dbUser = dbUser;
        this.db = db;

        fields = new BasicEventList<Field>();
        keys = new BasicEventList<Key>();
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

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getDbUser() {
        return dbUser;
    }

    public void setDbUser(String dbUser) {
        this.dbUser = dbUser;
    }

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public EventList<Field> getFields() {
        return fields;
    }

    public EventList<Key> getKeys() {
        return keys;
    }

    public void addField(Field field){
        fields.add(field);
    }

    public void addKey(Key key){
        keys.add(key);
    }

    public int getNumber() {
        return number;
    }
}
