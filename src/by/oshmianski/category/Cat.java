package by.oshmianski.category;

/**
 * Created with IntelliJ IDEA.
 * User: oshmianski
 * Date: 27.05.13
 * Time: 16:22
 */
public class Cat {
    private boolean isSynthetic;
    private String mainCat;

    /**
     * parentCat - все предыдущие категории одной строкой
     */
    private String parentCat;

    /**
     * indentLevel - глубина категории
     * начинается с 1 = первая категория
     * -1 - для реальных записей (документов)
     */
    private int indentLevel;

    /**
     * sortLevel -
     */
    private int sortLevel;

    public Cat() {
        isSynthetic = false;
        mainCat = "1";
        parentCat = "";
        indentLevel = -1;
        sortLevel = 0;
    }

    public boolean isSynthetic() {
        return isSynthetic;
    }

    public void setSynthetic(boolean synthetic) {
        isSynthetic = synthetic;
    }

    public String getMainCat() {
        return mainCat;
    }

    public void setMainCat(String mainCat) {
        this.mainCat = mainCat;
    }

    public String getParentCat() {
        return parentCat;
    }

    public void setParentCat(String parentCat) {
        this.parentCat = parentCat;
    }

    public int getIndentLevel() {
        return indentLevel;
    }

    public void setIndentLevel(int indentLevel) {
        this.indentLevel = indentLevel;
    }

    public int getSortLevel() {
        return sortLevel;
    }

    public void setSortLevel(int sortLevel) {
        this.sortLevel = sortLevel;
    }
}
