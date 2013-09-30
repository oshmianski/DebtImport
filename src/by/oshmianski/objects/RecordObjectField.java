package by.oshmianski.objects;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Created with IntelliJ IDEA.
 * User: VintsalovichS
 * Date: 30.09.13
 * Time: 12:54
 */
public class RecordObjectField extends DefaultMutableTreeNode {
    private String title;
    private String value;
    private RecordNodeFieldType type;

    public RecordObjectField(String title, String value, RecordNodeFieldType type) {
        this.title = title;
        this.value = value;
        this.type = type;
    }

    public RecordNodeFieldType getType() {
        return type;
    }

    public void setType(RecordNodeFieldType type) {
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

    @Override
    public String toString() {
        return title;
    }
}
