package by.oshmianski.objects;

import by.oshmianski.docks.*;
import by.oshmianski.ui.edt.UIProcessor;
import by.oshmianski.ui.edt.UIProcessorImport;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: VintsalovichS
 * Date: 9/16/13
 * Time: 2:35 PM
 */
public class UIProcessorImportImpl implements UIProcessorImport {
    private DockInfo dockInfo;
    private DockHeader dockHeader;
    private DockDataMain dockDataMain;
    private DockDataChild dockDataChild;
    private DockActions dockActions;

    public UIProcessorImportImpl(DockHeader dockHeader, DockActions dockActions, DockInfo dockInfo, DockDataMain dockDataMain, DockDataChild dockDataChild) {
        this.dockHeader = dockHeader;
        this.dockActions = dockActions;
        this.dockInfo = dockInfo;
        this.dockDataMain = dockDataMain;
        this.dockDataChild = dockDataChild;
    }

    @Override
    public void startLoading() {
        System.out.println("Start loading...");

        dockActions.bImportSetEnabled(false);
    }

    @Override
    public void stopLoading() {
        System.out.println("Stop loading");

        dockActions.bImportSetEnabled(true);
    }

    @Override
    public void progressSetValue(final int value) {
        dockInfo.progressSetValue(value);
    }

    @Override
    public void progressSetMaximum(final int count) {
        dockInfo.progressSetMaximum(count);
    }
}
