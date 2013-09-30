package by.oshmianski.objects;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import java.lang.*;
import java.lang.Object;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: VintsalovichS
 * Date: 30.09.13
 * Time: 12:48
 */
public class RecordObject extends DefaultMutableTreeNode{
    private String title;

    private ArrayList<RecordObjectField> fields;

    public RecordObject(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<RecordObjectField> getFields() {
        return fields;
    }

    public void setFields(ArrayList<RecordObjectField> fields) {
        this.fields = fields;
    }

    public void addField(RecordObjectField field){
        fields.add(field);
    }

    @Override
    public Object getUserObject() {
        return this;
    }

    @Override
    public TreeNode getChildAt(int index) {
        return fields.get(index);
    }

    @Override
    public int getChildCount() {
        return fields.size();
    }

    @Override
    public String toString() {
        return title;
    }
}
