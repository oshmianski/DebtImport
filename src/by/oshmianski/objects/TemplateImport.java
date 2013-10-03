package by.oshmianski.objects;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;

/**
 * Created with IntelliJ IDEA.
 * User: VintsalovichS
 * Date: 9/18/13
 * Time: 1:52 PM
 */
public class TemplateImport {
    private String unid;
    private String title;
    private String description;

    private EventList<Object> objects = new BasicEventList<Object>();
    private EventList<Link> links = new BasicEventList<Link>();

    public TemplateImport(String unid, String title, String description) {
        this.unid = unid;
        this.title = title;
        this.description = description;
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

    public Object getObjectByFormName(String formName){
        if(objects == null) return null;

        for(Object object : objects){
            if(formName.equals(object.getFormName())) return object;
        }

        return null;
    }

    @Override
    public String toString() {
        return getTitle();
    }
}
