package by.oshmianski.objects.addressParser;

/**
 * Created by vintselovich on 24.12.13.
 */
public class ParseItem {
    private String text;
    private String charBefore;
    private String charAfter;
    private ParseItemType type;
    private ParseItemTypeValue typeValue;
    private ParseItemTypeValue foundInCategory;
    private boolean beginWithNumber;
    private boolean index;

    public ParseItem() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCharBefore() {
        return charBefore;
    }

    public void setCharBefore(String charBefore) {
        this.charBefore = charBefore;
    }

    public String getCharAfter() {
        return charAfter;
    }

    public void setCharAfter(String charAfter) {
        this.charAfter = charAfter;
    }

    public ParseItemType getType() {
        return type;
    }

    public void setType(ParseItemType type) {
        this.type = type;
    }

    public ParseItemTypeValue getTypeValue() {
        return typeValue;
    }

    public void setTypeValue(ParseItemTypeValue typeValue) {
        this.typeValue = typeValue;
    }

    public ParseItemTypeValue getFoundInCategory() {
        return foundInCategory;
    }

    public void setFoundInCategory(ParseItemTypeValue foundInCategory) {
        this.foundInCategory = foundInCategory;
    }

    public boolean isBeginWithNumber() {
        return beginWithNumber;
    }

    public void setBeginWithNumber(boolean beginWithNumber) {
        this.beginWithNumber = beginWithNumber;
    }

    public boolean isIndex() {
        return index;
    }

    public void setIndex(boolean index) {
        this.index = index;
    }
}
