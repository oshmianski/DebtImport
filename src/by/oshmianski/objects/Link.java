package by.oshmianski.objects;

/**
 * Created with IntelliJ IDEA.
 * User: VintsalovichS
 * Date: 9/18/13
 * Time: 1:51 PM
 */
public class Link {
    private String unid;
    private String title;
    private String description;
    private String type;

    private Object mainObject;
    private Object childObject;
    private boolean makeResponse;
    private String responseField;
    private String responseFieldCustom;

    private String linkFormTitle;
    private String linkField1;
    private String linkField2;
    private String descrField1;
    private String descrField2;

    public Link(
            String unid,
            String title,
            String description,
            String type,
            Object mainObject,
            Object childObject,
            boolean makeResponse,
            String responseField,
            String responseFieldCustom,
            String linkFormTitle,
            String linkField1,
            String linkField2,
            String descrField1,
            String descrField2) {
        this.unid = unid;
        this.title = title;
        this.description = description;
        this.type = type;
        this.mainObject = mainObject;
        this.childObject = childObject;
        this.makeResponse = makeResponse;
        this.responseField = responseField;
        this.responseFieldCustom = responseFieldCustom;
        this.linkFormTitle = linkFormTitle;
        this.linkField1 = linkField1;
        this.linkField2 = linkField2;
        this.descrField1 = descrField1;
        this.descrField2 = descrField2;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getMainObject() {
        return mainObject;
    }

    public void setMainObject(Object mainObject) {
        this.mainObject = mainObject;
    }

    public Object getChildObject() {
        return childObject;
    }

    public void setChildObject(Object childObject) {
        this.childObject = childObject;
    }

    public String getResponseField() {
        return responseField;
    }

    public void setResponseField(String responseField) {
        this.responseField = responseField;
    }

    public String getResponseFieldCustom() {
        return responseFieldCustom;
    }

    public void setResponseFieldCustom(String responseFieldCustom) {
        this.responseFieldCustom = responseFieldCustom;
    }

    public String getLinkFormTitle() {
        return linkFormTitle;
    }

    public void setLinkFormTitle(String linkFormTitle) {
        this.linkFormTitle = linkFormTitle;
    }

    public String getLinkField1() {
        return linkField1;
    }

    public void setLinkField1(String linkField1) {
        this.linkField1 = linkField1;
    }

    public String getLinkField2() {
        return linkField2;
    }

    public void setLinkField2(String linkField2) {
        this.linkField2 = linkField2;
    }

    public String getDescrField1() {
        return descrField1;
    }

    public void setDescrField1(String descrField1) {
        this.descrField1 = descrField1;
    }

    public String getDescrField2() {
        return descrField2;
    }

    public void setDescrField2(String descrField2) {
        this.descrField2 = descrField2;
    }

    public boolean isMakeResponse() {
        return makeResponse;
    }

    public void setMakeResponse(boolean makeResponse) {
        this.makeResponse = makeResponse;
    }
}
