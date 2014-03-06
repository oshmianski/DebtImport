package by.oshmianski.loaders;

import by.oshmianski.objects.*;
import by.oshmianski.objects.Object;
import by.oshmianski.ui.edt.UIProcessor;
import by.oshmianski.utils.MyLog;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.SortedList;
import lotus.domino.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.util.*;

public class LoadImportData implements Runnable, Loader {
    private boolean executed = false;
    private boolean canceled = false;

    /**
     * UI callback
     */
    private UIProcessor ui;
    private boolean isTest;
    private Importer importer;

    private String moduleName;

    public LoadImportData(UIProcessor ui, boolean isTest) {
        super();

        this.ui = ui;
        this.isTest = isTest;

        moduleName = "Загрузка данных";

        importer = new Importer(this);
    }

    /**
     * Starts this loader execution in separate thread
     */
    @Override
    public synchronized void execute() {
        if (executed) {
//            throw new IllegalStateException("Loader is already executed");
            return;
        }
        executed = true;
        canceled = false;
        Thread t = new Thread(this, "Custom loader thread " + moduleName);
        t.start();
    }

    /**
     * Loader main cycle
     */
    @Override
    public void run() {
        //действия вначале
//        ui.startLoading();

        try {
            work();
        } catch (Exception e) {
            MyLog.add2Log(e);
        } finally {
            //окончание
            executed = false;
//            ui.stopLoading();
        }
    }

    /**
     * Cancels current loading
     */
    @Override
    public synchronized void cancel() {
        canceled = true;
    }

    public synchronized boolean isCanceled() {
        return canceled;
    }

    protected void work() {
        importer.process();
    }

    public UIProcessor getUi() {
        return ui;
    }

    public boolean isTest() {
        return isTest;
    }

    public Importer getImporter() {
        return importer;
    }

    public void setTest(boolean isTest) {
        this.isTest = isTest;
    }

    public boolean isExecuted() {
        return executed;
    }
}
