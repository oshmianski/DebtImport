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
    public void startLoading() {
        System.out.println("Start loading...");

        clearDataImport();

        dockActions.bTestSetEnabled(false);
    }

    @Override
    public void stopLoading() {
        System.out.println("Stop loading");

        dockActions.bTestSetEnabled(true);
    }

    @Override
    public void appendDataImport(DataMainItem dataMainItem) {
        dockDataMain.appendDataMain(dataMainItem);

        dockInfo.countIncAll();

        if (dataMainItem.getStatus() == Status.OK) {
            dockInfo.countIncOk();
        }
        if (dataMainItem.getStatus() == Status.ERROR) {
            dockInfo.countIncError();
        }
        if (dataMainItem.getStatus() == Status.WARNING) {
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
    public void progressSetValue(final int value) {
        dockInfo.progressSetValue(value);
    }

    @Override
    public void progressSetMaximum(final int count) {
        dockInfo.progressSetMaximum(count);
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

        if(dockHeader.getTemplateImport() == null){
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
    public JTextField getFileField() {
        JTextField val = dockHeader.getFileField();
        return val;
    }

    @Override
    public JTextField getCol2Description() {
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
}
