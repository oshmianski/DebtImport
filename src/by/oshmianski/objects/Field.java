package by.oshmianski.objects;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.SortedList;

/**
 * Created with IntelliJ IDEA.
 * User: VintsalovichS
 * Date: 9/18/13
 * Time: 1:38 PM
 */
public class Field {
    private String unid;
    private String titleSys;
    private String titleUser;
    private String description;
    private String type;
    private String xmlCell;
    private boolean emptyFlag;

    private EventList<Rule> rules = new BasicEventList<Rule>();

    public Field(String unid, String titleSys, String titleUser, String description, String type, String xmlCell, boolean emptyFlag) {
        this.unid = unid;
        this.titleSys = titleSys;
        this.titleUser = titleUser;
        this.description = description;
        this.type = type;
        this.xmlCell = xmlCell;
        this.emptyFlag = emptyFlag;
    }

    public String getUnid() {
        return unid;
    }

    public void setUnid(String unid) {
        this.unid = unid;
    }

    public String getTitleSys() {
        return titleSys;
    }

    public void setTitleSys(String titleSys) {
        this.titleSys = titleSys;
    }

    public String getTitleUser() {
        return titleUser;
    }

    public void setTitleUser(String titleUser) {
        this.titleUser = titleUser;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getXmlCell() {
        return xmlCell;
    }

    public void setXmlCell(String xmlCell) {
        this.xmlCell = xmlCell;
    }

    public EventList<Rule> getRules() {
        return rules;
    }

    public void addRule(Rule rule) {
        rules.add(rule);
    }

    public boolean isEmptyFlag() {
        return emptyFlag;
    }

    public void setEmptyFlag(boolean emptyFlag) {
        this.emptyFlag = emptyFlag;
    }

    public void setRules(EventList<Rule> rules) {
        this.rules = rules;
    }
}
