package by.oshmianski.loaders;

import by.oshmianski.objects.DataMainItem;
import by.oshmianski.ui.edt.UIProcessor;
import by.oshmianski.utils.MyLog;
import lotus.domino.*;

public class LoadDataToDB implements Runnable, Loader {
    private boolean executed = false;
    private boolean canceled = false;

    private String moduleName;

    private UIProcessor ui;

    public LoadDataToDB(UIProcessor ui) {
        super();

        this.ui = ui;

        moduleName = "Импорт данных";
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
        Thread t = new Thread(this, "Custom loader thread " + moduleName);
        t.start();
    }

    /**
     * Loader main cycle
     */
    @Override
    public void run() {
        //действия вначале
        ui.startLoadingToDB();

        try {
            work();
        } catch (Exception e) {
            MyLog.add2Log(e);
        } finally {
            //окончание
            executed = false;
            ui.stopLoadingToDB();
        }
    }

    /**
     * Cancels current loading
     */
    @Override
    public synchronized void cancel() {
        canceled = true;
    }

    private synchronized boolean isCanceled() {
        return canceled;
    }

    protected void work() {
        Session session = null;
        Database db = null;
        View view = null;
        ViewNavigator nav = null;
        ViewEntry ve = null;
        ViewEntry vetmp = null;

        try {
            NotesThread.sinitThread();
            session = NotesFactory.createSession();
            db = session.getDatabase(null, null);

            int i = 0;
            ui.setProgressLabelText("Создание объектов...");
            ui.setProgressMaximum(ui.getDataMainItems().size());
            ui.setProgressValue(0);
            for(DataMainItem dataMainItem1 : ui.getDataMainItems()){

                Thread.sleep(5);

                if (canceled) break;

                i++;
                ui.setProgressValue(i);
            }
            ui.setProgressLabelText("Создание объектов...OK");


        } catch (Exception e) {
            MyLog.add2Log(e);
        } finally {
            try {
                if (vetmp != null) {
                    vetmp.recycle();
                }
                if (ve != null) {
                    ve.recycle();
                }
                if (nav != null) {
                    nav.recycle();
                }
                if (view != null) {
                    view.recycle();
                }
                if (db != null) {
                    db.recycle();
                }
                if (session != null) {
                    session.recycle();
                }
            } catch (Exception e) {
                MyLog.add2Log(e);
            }

            try {
                NotesThread.stermThread();
            } catch (Exception e) {
                MyLog.add2Log(e);
            }
        }
    }
}
