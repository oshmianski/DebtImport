package by.oshmianski.loaders;

import by.oshmianski.objects.UIProcessorImpl;
import by.oshmianski.utils.MyLog;
import lotus.domino.*;

public class LoadImportData implements Runnable, Loader {
    private boolean executed = false;
    private boolean canceled = false;

    /** UI callback */
    protected UIProcessorImpl ui;

    private String moduleName;

    public LoadImportData(UIProcessorImpl ui) {
        super();

        this.ui = ui;

        moduleName = "Загрузка данных";
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
