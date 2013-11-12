package by.oshmianski.objects;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;

/**
 * Created with IntelliJ IDEA.
 * User: VintsalovichS
 * Date: 9/18/13
 * Time: 1:38 PM
 */
public class Field {
    private int num;
    private String unid;
    private String titleSys;
    private String titleUser;
    private String description;
    private TYPE type;
    private String xmlCell;
    private boolean emptyFlag;
    private boolean emptyFlagSignal;
    private boolean fake;

    public static enum TYPE {
        TEXT,
        NUMBER,
        DATETIME,
        AUTHORS,
        READERS
    }

    private EventList<Rule> rules = new BasicEventList<Rule>();

    public Field(
            int num,
            String unid,
            String titleSys,
            String titleUser,
            String description,
            String type,
            String xmlCell,
            boolean emptyFlag,
            boolean emptyFlagSignal,
            boolean fake) {
        this.num = num;
        this.unid = unid;
        this.titleSys = titleSys;
        this.titleUser = titleUser;
        this.description = description;
        this.xmlCell = xmlCell;
        this.emptyFlag = emptyFlag;
        this.emptyFlagSignal = emptyFlagSignal;
        this.fake = fake;

        if ("text".equals(type))
            this.type = TYPE.TEXT;
        if ("number".equals(type))
            this.type = TYPE.NUMBER;
        if ("datetime".equals(type))
            this.type = TYPE.DATETIME;
        if ("authors".equals(type))
            this.type = TYPE.AUTHORS;
        if ("readers".equals(type))
            this.type = TYPE.READERS;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
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

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
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

    public boolean isEmptyFlagSignal() {
        return emptyFlagSignal;
    }

    public void setEmptyFlagSignal(boolean emptyFlagSignal) {
        this.emptyFlagSignal = emptyFlagSignal;
    }

    public boolean isFake() {
        return fake;
    }

    public void setFake(boolean fake) {
        this.fake = fake;
    }
}
