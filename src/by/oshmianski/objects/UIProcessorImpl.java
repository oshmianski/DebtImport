package by.oshmianski.objects;

import by.oshmianski.docks.*;
import by.oshmianski.ui.edt.UIProcessor;
import ca.odell.glazedlists.EventList;

import javax.swing.*;
import java.util.ArrayList;

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

    public UIProcessorImpl(
            DockHeader dockHeader,
            DockActions dockActions,
            DockInfo dockInfo,
            DockDataMain dockDataMain,
            DockDataChild dockDataChild,
            DockObjectTree dockObjectTree) {

        this.dockHeader = dockHeader;
        this.dockActions = dockActions;
        this.dockInfo = dockInfo;
        this.dockDataMain = dockDataMain;
        this.dockDataChild = dockDataChild;
        this.dockObjectTree = dockObjectTree;
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
        }
        if (dataMainItem.getStatusFromChild() == Status.ERROR) {
            dockInfo.countIncError();
        }
        if (dataMainItem.getStatusFromChild() == Status.WARNING_ALREADY_EXIST_IN_DB
                || dataMainItem.getStatusFromChild() == Status.WARNING_ALREADY_EXIST_IN_PREVIOUS
                || dataMainItem.getStatusFromChild() == Status.WARNING_OBJECT_WILL_NOT_CREATE
                || dataMainItem.getStatusFromChild() == Status.WARNING_EMPTY_FIELD
                || dataMainItem.getStatusFromChild() == Status.WARNING_ADDRESS_NO_INDEX
                || dataMainItem.getStatusFromChild() == Status.WARNING_ADDRESS_NO_COUTRY
                || dataMainItem.getStatusFromChild() == Status.WARNING_ADDRESS_NO_REGION
                || dataMainItem.getStatusFromChild() == Status.WARNING_ADDRESS_NO_DISTRICT
                || dataMainItem.getStatusFromChild() == Status.WARNING_ADDRESS_NO_STREET_TYPE
                || dataMainItem.getStatusFromChild() == Status.WARNING_ADDRESS_NO_STREET
                || dataMainItem.getStatusFromChild() == Status.WARNING_ADDRESS_NO_CITY_TYPE
                || dataMainItem.getStatusFromChild() == Status.WARNING_ADDRESS_NO_CITY
                || dataMainItem.getStatusFromChild() == Status.WARNING_ADDRESS_NO_HOUSE
                || dataMainItem.getStatusFromChild() == Status.WARNING_PASSPORT_NO_TYPE
                || dataMainItem.getStatusFromChild() == Status.WARNING_PASSPORT_NO_NUM
                || dataMainItem.getStatusFromChild() == Status.WARNING_PASSPORT_NO_DATE
                || dataMainItem.getStatusFromChild() == Status.WARNING_PASSPORT_NO_ORG
                ) {
            dockInfo.countIncWarning();
        }
    }

    @Override
    public void clearDataImport() {
        dockDataMain.clearDataMain();
        dockDataChild.clearDataChild();
        dockObjectTree.cleareObject();
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
    public void setDockDataChildItems(ArrayList<DataChildItem> dockDataChildItems) {
        dockDataChild.setDataChildItems(dockDataChildItems);
    }

    @Override
    public void setDockObjectTreeObjects(ArrayList<RecordObject> objects) {
        dockObjectTree.setObjects(objects);
    }

    @Override
    public int getStartFrom() {
        return Integer.valueOf(dockHeader.getStartFrom().getText());
    }

    @Override
    public int getEndTo() {
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
