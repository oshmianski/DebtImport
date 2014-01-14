package by.oshmianski.objects;

import by.oshmianski.docks.*;
import by.oshmianski.ui.edt.UIProcessor;
import ca.odell.glazedlists.EventList;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: VintsalovichS
 * Date: 9/16/13
 * Time: 2:35 PM
 */
public class UIProcessorImpl implements UIProcessor {
    private DockInfo dockInfo;
    private DockHeader dockHeader;
    private DockDataMain dockDataMain;
    private DockDataChild dockDataChild;
    private DockActions dockActions;
    private DockObjectTree dockObjectTree;
    private DockAddressParser dockAddressParser;

    public UIProcessorImpl(
            DockHeader dockHeader,
            DockActions dockActions,
            DockInfo dockInfo,
            DockDataMain dockDataMain,
            DockDataChild dockDataChild,
            DockObjectTree dockObjectTree,
            DockAddressParser dockAddressParser) {

        this.dockHeader = dockHeader;
        this.dockActions = dockActions;
        this.dockInfo = dockInfo;
        this.dockDataMain = dockDataMain;
        this.dockDataChild = dockDataChild;
        this.dockObjectTree = dockObjectTree;
        this.dockAddressParser = dockAddressParser;
    }

    @Override
    public void setFilteredCount() {
        dockDataMain.setFilteredCount();
    }

    @Override
    public void startLoadingTI() {
        System.out.println("Start loading TI...");
        dockHeader.startLoadTI();
    }

    @Override
    public void stopLoadingTI() {
        System.out.println("Stop loading TI");
        dockHeader.stopLoadTI();
    }

    @Override
    public void setTemplateImports(EventList<TemplateImport> templateImports) {
        dockHeader.setTemplateImports(templateImports);
    }

    @Override
    public void setProgressLabelText(String text) {
        dockInfo.setProgressLabelText(text);
    }

    @Override
    public void setProgressValue(int count) {
        dockInfo.progressSetValue(count);
    }

    @Override
    public void setProgressMaximum(int maximum) {
        dockInfo.progressSetMaximum(maximum);
    }

    @Override
    public void appendDataImport(DataMainItem dataMainItem) {
        dockDataMain.appendDataMain(dataMainItem);

        dockInfo.countIncAll();

        if (dataMainItem.getStatusFromChild() == Status.OK || dataMainItem.getStatusFromChild() == Status.INFO) {
            dockInfo.countIncOk();

            return;
        }
        if (dataMainItem.getStatusFromChild() == Status.ERROR) {
            dockInfo.countIncError();

            return;
        }

        dockInfo.countIncWarning();
    }

    @Override
    public boolean isTestImport() {
        return dockHeader.isTestImport();
    }

    @Override
    public void clearDataImport() {
        dockDataMain.clearDataMain();
        dockDataChild.clearDataChild();
        dockObjectTree.cleareObject();
        dockAddressParser.clearItems();
        dockInfo.progressSetValue(0);
        dockInfo.countClearAll();
    }

    @Override
    public boolean isHeaderCorrect() {
        if (dockHeader.getFileField().getText().isEmpty()) {
            JOptionPane.showMessageDialog(
                    null,
                    "Не выбран файл!",
                    "Внимание",
                    JOptionPane.ERROR_MESSAGE);

            return false;
        }

        if (dockHeader.getTemplateImport() == null) {
            JOptionPane.showMessageDialog(
                    null,
                    "Не выбран шаблон!",
                    "Внимание",
                    JOptionPane.ERROR_MESSAGE);

            return false;
        }

        return true;
    }

    @Override
    public boolean isCanImport() {
        return !(dockInfo.isError() || dockInfo.isWarning());
    }

    @Override
    public void setDockDataChildItems(DataMainItem dataMainItem) {
        dockDataChild.setDataChildItems(dataMainItem);
    }

    @Override
    public void setDockAddressParserItems(DataMainItem dataMainItem) {
        dockAddressParser.setAddressParserItems(dataMainItem);
    }

    @Override
    public void setDockObjectTreeObjects(DataMainItem dataMainItem) {
        dockObjectTree.setObjects(dataMainItem);
    }

    @Override
    public int getStartFrom() {
        return Integer.valueOf(dockHeader.getStartFrom().getText());
    }

    @Override
    public int getEndTo() {
        if (dockHeader.getEndTo().getText().isEmpty()) return -1;
        return Integer.valueOf(dockHeader.getEndTo().getText());
    }

    @Override
    public JTextField getFileField() {
        JTextField val = dockHeader.getFileField();
        return val;
    }

    @Override
    public String getCol2Description() {
        return dockHeader.getCol2Description();
    }

    @Override
    public TemplateImport getTemplateImport() {
        return dockHeader.getTemplateImport();
    }

    @Override
    public void setCountAll2Import(int count) {
        dockInfo.setCountAll2Import(count);
    }

    @Override
    public EventList<DataMainItem> getDataMainItems() {
        return dockDataMain.getDataMainItems();
    }

    @Override
    public void setButtonTestStartEnable(boolean enable) {
        dockActions.setButtomTestStartEnable(enable);
    }

    @Override
    public void setButtonTestStopEnable(boolean enable) {
        dockActions.setButtomTestStopEnable(enable);
    }

    @Override
    public void setButtonImportStartEnable(boolean enable) {
        dockActions.setButtomImportStartEnable(enable);
    }

    @Override
    public void setButtonImportStopEnable(boolean enable) {
        dockActions.setButtomImportStopEnable(enable);
    }

    @Override
    public void countIncImported() {
        dockInfo.countIncImported();
    }

    @Override
    public EventList<CellHeader> getCellHeaders() {
        return dockHeader.getCellHeaders();
    }
}
