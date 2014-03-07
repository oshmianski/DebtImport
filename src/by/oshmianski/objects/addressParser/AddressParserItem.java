package by.oshmianski.objects.addressParser;

/**
 * Created by vintselovich on 24.12.13.
 */
public class AddressParserItem implements Comparable<AddressParserItem> {
    private int number;
    private String text;
    private String charBefore;
    private String charAfter;
    private AddressParserItemType type;
    private AddressParserItemTypeValue typeValue;
    private String typeValue2;
    private AddressParserItemTypeValue foundInCategory;
    private boolean beginWithNumber;
    private boolean index;
    private boolean processed;
    private boolean processedWithoutValue;
    private AddressParserOperation operation;

    public AddressParserItem(int number, String text) {
        this.number = number;
        this.text = text;
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

    public AddressParserItemType getType() {
        return type;
    }

    public void setType(AddressParserItemType type) {
        this.type = type;
    }

    public AddressParserItemTypeValue getTypeValue() {
        return typeValue;
    }

    public void setTypeValue(AddressParserItemTypeValue typeValue) {
        this.typeValue = typeValue;
    }

    public AddressParserItemTypeValue getFoundInCategory() {
        return foundInCategory;
    }

    public void setFoundInCategory(AddressParserItemTypeValue foundInCategory) {
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

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed, AddressParserOperation operation) {
        this.processed = processed;
        this.operation = operation;
    }

    public String getTypeValue2() {
        return typeValue2;
    }

    public boolean isService() {
        return type == AddressParserItemType.service;
    }

    public void setTypeValue2(String typeValue2) {
        this.typeValue2 = typeValue2;
    }

    public boolean isProcessedWithoutValue() {
        return processedWithoutValue;
    }

    public void setProcessedWithoutValue(boolean processedWithoutValue) {
        this.processedWithoutValue = processedWithoutValue;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    public AddressParserOperation getOperation() {
        return operation;
    }

    public void setOperation(AddressParserOperation operation) {
        this.operation = operation;
    }


    @Override
    public int compareTo(AddressParserItem o) {
        final int BEFORE = -1;
        final int EQUAL = 0;
        final int AFTER = 1;

        if (getNumber() == o.getNumber()) return EQUAL;
        if (getNumber() > o.getNumber()) return AFTER;
        if (getNumber() < o.getNumber()) return BEFORE;

        return EQUAL;
    }
}
