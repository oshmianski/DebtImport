package by.oshmianski.objects;

/**
 * Created with IntelliJ IDEA.
 * User: VintsalovichS
 * Date: 9/18/13
 * Time: 1:44 PM
 */
public class Key {
    private String unid;
    private String title;
    private String description;
    private String dbUser;
    private String db;
    private String view;
    private String pref1;
    private String pref2;
    private String pref3;
    private String pref4;
    private String pref5;
    private Field field1;
    private Field field2;
    private Field field3;
    private Field field4;
    private Field field5;
    private boolean checkPassport;

    public Key(String unid, String title, String description, String dbUser, String db, String view, String pref1, String pref2, String pref3, String pref4, String pref5, boolean checkPassport) {
        this.unid = unid;
        this.title = title;
        this.description = description;
        this.dbUser = dbUser;
        this.db = db;
        this.view = view;
        this.pref1 = pref1;
        this.pref2 = pref2;
        this.pref3 = pref3;
        this.pref4 = pref4;
        this.pref5 = pref5;
        this.checkPassport = checkPassport;
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

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public String getPref1() {
        return pref1;
    }

    public void setPref1(String pref1) {
        this.pref1 = pref1;
    }

    public String getPref2() {
        return pref2;
    }

    public void setPref2(String pref2) {
        this.pref2 = pref2;
    }

    public String getPref3() {
        return pref3;
    }

    public void setPref3(String pref3) {
        this.pref3 = pref3;
    }

    public String getPref4() {
        return pref4;
    }

    public void setPref4(String pref4) {
        this.pref4 = pref4;
    }

    public String getPref5() {
        return pref5;
    }

    public void setPref5(String pref5) {
        this.pref5 = pref5;
    }

    public Field getField1() {
        return field1;
    }

    public void setField1(Field field1) {
        this.field1 = field1;
    }

    public Field getField2() {
        return field2;
    }

    public void setField2(Field field2) {
        this.field2 = field2;
    }

    public Field getField3() {
        return field3;
    }

    public void setField3(Field field3) {
        this.field3 = field3;
    }

    public Field getField4() {
        return field4;
    }

    public void setField4(Field field4) {
        this.field4 = field4;
    }

    public Field getField5() {
        return field5;
    }

    public void setField5(Field field5) {
        this.field5 = field5;
    }

    public boolean isCheckPassport() {
        return checkPassport;
    }

    public void setCheckPassport(boolean checkPassport) {
        this.checkPassport = checkPassport;
    }
}
