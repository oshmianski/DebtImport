package by.oshmianski.objects;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: oshmianski
 * Date: 30.09.13
 * Time: 21:14
 */
public class RecordObjectRoot {
    private String title;
    private ArrayList<RecordObject> objects;

    public RecordObjectRoot(String title) {
        this.title = title;
        objects = new ArrayList<RecordObject>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<RecordObject> getObjects() {
        return objects;
    }

    public void setObjects(ArrayList<RecordObject> objects) {
        this.objects = objects;
    }

    public void addObject(RecordObject object) {
        objects.add(object);
    }

    @Override
    public String toString() {
        return title;
    }
}
