package by.oshmianski.loaders;

import by.oshmianski.objects.DataMainItem;
import by.oshmianski.objects.Status;
import by.oshmianski.ui.edt.UIProcessor;
import by.oshmianski.ui.edt.UIProcessorImport;
import by.oshmianski.utils.MyLog;
import lotus.domino.*;

public class LoadDataTest implements Runnable, Loader {
    private boolean executed = false;
    private boolean canceled = false;

    /**
     * UI callback
     */
    protected UIProcessorImport ui;

    private String moduleName;

    public LoadDataTest(UIProcessorImport ui) {
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
        ui.startLoading();

        try {
            work();
        } catch (Exception e) {
            MyLog.add2Log(e);
        } finally {
            //окончание
            executed = false;
            ui.stopLoading();
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

            int count = 10;

            ui.progressSetValue(0);
            ui.progressSetMaximum(count);

            for (int i = 1; i <= count; i++) {
                ui.progressSetValue(i);

                Thread.sleep(500);

                if(canceled) break;
            }
//            db.openByReplicaID(AppletParams.getInstance().getServer(), AppletParams.getInstance().getDbreplicaid());
//            view = db.getView(AppletParams.getInstance().getViewItem());
//            view.setAutoUpdate(false);
//
//            nav = view.createViewNav();
//
//            if (Integer.valueOf(session.evaluate("@Version").firstElement().toString()) >= 379) {
//                nav.setBufferMaxEntries(400);
//                nav.setEntryOptions(ViewNavigator.VN_ENTRYOPT_NOCOUNTDATA);
//            }
//
//            ve = nav.getFirst();
//            while (ve != null) {
//                if (ve.isCategory())
//                    ui.appendItemToTree(new ItemCat(true, ve.getColumnValues().elementAt(0).toString(),
//                            "",
//                            "",
//                            "",
//                            ""));
//                else
//                    ui.appendItemToTree(new ItemCat(
//                            false, ve.getColumnValues().elementAt(0).toString(),
//                            "",
//                            ve.getColumnValues().elementAt(1).toString(),
//                            ve.getColumnValues().elementAt(2).toString(),
//                            ve.getUniversalID()));
//
//                vetmp = nav.getNext();
//                ve.recycle();
//                ve = vetmp;
//            }


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
