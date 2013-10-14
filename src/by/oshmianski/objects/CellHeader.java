package by.oshmianski.objects;

/**
 * Created with IntelliJ IDEA.
 * User: oshmianski
 * Date: 13.10.13
 * Time: 20:58
 */
public class CellHeader {
    private String colTitle;
    private String colValue;

    public CellHeader(String colTitle, String colValue) {
        this.colTitle = colTitle;
        this.colValue = colValue;
    }

    public String getColTitle() {
        return colTitle;
    }

    public void setColTitle(String colTitle) {
        this.colTitle = colTitle;
    }

    public String getColValue() {
        return colValue;
    }

    public void setColValue(String colValue) {
        this.colValue = colValue;
    }

    @Override
    public String toString() {
        return colTitle + " | " + colValue;
    }
}
