package by.oshmianski.ui.edt;

import by.oshmianski.objects.*;
import ca.odell.glazedlists.EventList;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: 8-058
 * Date: 05.04.13
 * Time: 9:59
 */
public interface UIProcessor {
    @RequiresEDT
    void setFilteredCount();

    @RequiresEDT
    void setProgressLabelText(String text);

    @RequiresEDT
    void setButtonTestStartEnable(boolean enable);

    @RequiresEDT
    void setButtonTestStopEnable(boolean enable);

    @RequiresEDT
    void setButtonImportStartEnable(boolean enable);

    @RequiresEDT
    void setButtonImportStopEnable(boolean enable);

    @RequiresEDT
    void startLoadingTI();

    @RequiresEDT
    void appendDataImport(DataMainItem dataMainItem);

    @RequiresEDT
    void countIncImported();

//    @RequiresEDT
    EventList<DataMainItem> getDataMainItems();

    @RequiresEDT
    void setTemplateImports(EventList<TemplateImport> templateImports);

    @RequiresEDT
    void stopLoadingTI();

    @RequiresEDT
    void clearDataImport();

    @RequiresEDT
    void setProgressValue(final int count);

    @RequiresEDT
    void setProgressMaximum(final int maximum);

    @RequiresEDT
    boolean isHeaderCorrect();

    boolean isTestImport();

    @RequiresEDT
    boolean isCanImport();

    @RequiresEDT
    void setDockDataChildItems(DataMainItem dataMainItem);

    @RequiresEDT
    void setDockAddressParserItems(DataMainItem dataMainItem);

    @RequiresEDT
    void setDockObjectTreeObjects(DataMainItem dataMainItem);

//    @RequiresEDT
    int getStartFrom();

    int getEndTo();

    int getSheetNum();

//    @RequiresEDT
    JTextField getFileField();

//    @RequiresEDT
    String getCol2Description();

//    @RequiresEDT
    TemplateImport getTemplateImport();

    EventList<CellHeader> getCellHeaders();

    @RequiresEDT
    void setCountAll2Import(int count);
}
