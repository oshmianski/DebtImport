package by.oshmianski.objects;

/**
 * Created with IntelliJ IDEA.
 * User: VintsalovichS
 * Date: 30.09.13
 * Time: 12:54
 */
public class RecordObjectField {
    private String title;
    private String titleUser;
    private String value;
    private Field.TYPE type;
    private boolean multiple;

    public RecordObjectField(
            String title,
            String titleUser,
            String value,
            Field.TYPE type,
            boolean multiple) {
        this.title = title;
        this.titleUser = titleUser;
        this.value = value;
        this.type = type;
        this.multiple = multiple;
    }

    public Field.TYPE getType() {
        return type;
    }

    public void setType(Field.TYPE type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleUser() {
        return titleUser;
    }

    public void setTitleUser(String titleUser) {
        this.titleUser = titleUser;
    }

    public boolean isMultiple() {
        return multiple;
    }

    public void setMultiple(boolean multiple) {
        this.multiple = multiple;
    }

    @Override
    public String toString() {
        return title;
    }
}
