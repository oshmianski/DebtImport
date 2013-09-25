package by.oshmianski.ui.edt;

import by.oshmianski.objects.DataChildItem;
import by.oshmianski.objects.DataMainItem;
import by.oshmianski.objects.TemplateImport;
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
    void startLoading();

    @RequiresEDT
    void stopLoading();

    @RequiresEDT
    void startLoadingTI();

    @RequiresEDT
    void appendDataImport(DataMainItem dataMainItem);

//    @RequiresEDT
    EventList<DataMainItem> getDataMainItems();

    @RequiresEDT
    void setTemplateImports(EventList<TemplateImport> templateImports);

    @RequiresEDT
    void stopLoadingTI();

    @RequiresEDT
    void clearDataImport();

    @RequiresEDT
    void progressSetValue(final int value);

    @RequiresEDT
    void progressSetMaximum(final int count);

    @RequiresEDT
    boolean isHeaderCorrect();

    @RequiresEDT
    boolean isCanImport();

    @RequiresEDT
    void setDockDataChildItems(ArrayList<DataChildItem> dockDataChildItems);

//    @RequiresEDT
    int getStartFrom();

//    @RequiresEDT
    JTextField getFileField();

//    @RequiresEDT
    JTextField getCol2Description();

//    @RequiresEDT
    TemplateImport getTemplateImport();

    @RequiresEDT
    void setCountAll2Import(int count);
}
