package by.oshmianski.loaders;

import by.oshmianski.objects.*;
import by.oshmianski.objects.Object;
import by.oshmianski.ui.edt.UIProcessor;
import by.oshmianski.utils.AppletParams;
import by.oshmianski.utils.MyLog;
import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import lotus.domino.*;

public class LoadTemplateImport implements Runnable, Loader {
    private boolean executed = false;
    private boolean canceled = false;

    /**
     * UI callback
     */
    protected UIProcessor ui;

    private String moduleName;

    public LoadTemplateImport(UIProcessor ui) {
        super();

        this.ui = ui;

        moduleName = "Загрузка шаблонов";
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
        ui.startLoadingTI();

        try {
            work();
        } catch (Exception e) {
            MyLog.add2Log(e);
        } finally {
            //окончание
            executed = false;
            ui.stopLoadingTI();
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
        View viewObject = null;
        View viewLink = null;
        View viewKey = null;
        View viewField = null;
        View viewRule = null;
        ViewNavigator nav = null;
        ViewEntry ve = null;
        ViewEntry vetmp = null;
        Document doc = null;

        DocumentCollection colObject = null;
        DocumentCollection colLink = null;

        Document docObject = null;
        Document docObjectTmp = null;
        Document docLink = null;
        Document docLinkTmp = null;

        Document docMainObject = null;
        Document docChildObject = null;

        try {
            NotesThread.sinitThread();
            session = NotesFactory.createSession();
            db = session.getDatabase(null, null);
            db.openByReplicaID(AppletParams.getInstance().getServer(), AppletParams.getInstance().getDbReplicaID());

            view = db.getView(AppletParams.getInstance().getViewTI());
            view.setAutoUpdate(false);

            viewObject = db.getView(AppletParams.getInstance().getViewObjectRef());
            viewObject.setAutoUpdate(false);
            viewLink = db.getView(AppletParams.getInstance().getViewLinkRef());
            viewLink.setAutoUpdate(false);
            viewKey = db.getView(AppletParams.getInstance().getViewKeyRef());
            viewKey.setAutoUpdate(false);
            viewField = db.getView(AppletParams.getInstance().getViewFieldRef());
            viewField.setAutoUpdate(false);
            viewRule = db.getView(AppletParams.getInstance().getViewRuleRef());
            viewRule.setAutoUpdate(false);

            nav = view.createViewNav();

            if (Integer.valueOf(session.evaluate("@Version").firstElement().toString()) >= 379) {
                nav.setBufferMaxEntries(400);
                nav.setEntryOptions(ViewNavigator.VN_ENTRYOPT_NOCOUNTDATA);
            }

            EventList<TemplateImport> templateImports = new BasicEventList<TemplateImport>();

            TemplateImport templateImport;
            Object objectMain;
            Object objectChild;
            Link link;

            ve = nav.getFirst();
            while (ve != null) {
                if (!ve.isCategory()) {
                    doc = ve.getDocument();

                    templateImport = new TemplateImport(ve.getUniversalID(), doc.getItemValueString("title"), doc.getItemValueString("description"));
                    templateImports.add(templateImport);

                    colObject = viewObject.getAllDocumentsByKey(doc.getUniversalID(), true);
                    docObject = colObject.getFirstDocument();
                    while ((docObject != null)) {

                        templateImport.addObject(produceObject(db, docObject, viewKey, viewField, viewRule));

                        docObjectTmp = colObject.getNextDocument();
                        docObject.recycle();
                        docObject = docObjectTmp;
                    }

                    colLink = viewLink.getAllDocumentsByKey(doc.getUniversalID(), true);
                    docLink = colLink.getFirstDocument();
                    while (docLink != null) {
                        objectMain = null;
                        objectChild = null;

                        if ("1".equals(docLink.getItemValueString("type"))) {
                            docMainObject = db.getDocumentByUNID(docLink.getItemValueString("mainObjectUNID"));
                            objectMain = produceObject(db, docMainObject, viewKey, viewField, viewRule);

                            docChildObject = db.getDocumentByUNID(docLink.getItemValueString("childObjectUNID"));
                            objectChild = produceObject(db, docChildObject, viewKey, viewField, viewRule);
                        }
                        link = new Link(
                                docLink.getUniversalID(),
                                docLink.getItemValueString("title"),
                                docLink.getItemValueString("description"),
                                docLink.getItemValueString("type"),
                                objectMain,
                                objectChild,
                                docLink.getItemValueString("responseField"),
                                docLink.getItemValueString("responseFieldCustom"),
                                docLink.getItemValueString("linkFormTitle"),
                                docLink.getItemValueString("linkField1"),
                                docLink.getItemValueString("linkField2"),
                                docLink.getItemValueString("descrField1"),
                                docLink.getItemValueString("descrField2")
                        );

                        templateImport.addLink(link);

                        docLinkTmp = colLink.getNextDocument();
                        docLink.recycle();
                        docLink = docLinkTmp;
                    }
                }

                vetmp = nav.getNext();
                ve.recycle();
                ve = vetmp;
            }

            ui.setTemplateImports(templateImports);
        } catch (Exception e) {
            MyLog.add2Log(e);
        } finally {
            try {
                if (docMainObject != null) {
                    docMainObject.recycle();
                }
                if (docChildObject != null) {
                    docChildObject.recycle();
                }
                if (doc != null) {
                    doc.recycle();
                }
                if (docObject != null) {
                    docObject.recycle();
                }
                if (docObjectTmp != null) {
                    docObjectTmp.recycle();
                }
                if (docLink != null) {
                    docLink.recycle();
                }
                if (docLinkTmp != null) {
                    docLinkTmp.recycle();
                }

                if (colObject != null) {
                    colObject.recycle();
                }
                if (colLink != null) {
                    colLink.recycle();
                }
                if (viewObject != null) {
                    viewObject.recycle();
                }
                if (viewLink != null) {
                    viewLink.recycle();
                }
                if (viewKey != null) {
                    viewKey.recycle();
                }
                if (viewField != null) {
                    viewField.recycle();
                }
                if (viewRule != null) {
                    viewRule.recycle();
                }
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

    private Object produceObject(Database db, Document docObject, View viewKeyRef, View viewFieldRef, View viewRuleRef) {
        Object object = null;

        DocumentCollection colKey = null;
        DocumentCollection colField = null;

        Document docKey = null;
        Document docKeyTmp = null;
        Document docField = null;
        Document docFieldTmp = null;

        Key key;

        try {
            object = new Object(
                    docObject.getUniversalID(),
                    docObject.getItemValueInteger("number"),
                    docObject.getItemValueString("title"),
                    docObject.getItemValueString("description"),
                    docObject.getItemValueString("formName"),
                    docObject.getItemValueString("dbUser"),
                    docObject.getItemValueString("db")
            );

            colKey = viewKeyRef.getAllDocumentsByKey(docObject.getUniversalID(), true);
            docKey = colKey.getFirstDocument();
            while (docKey != null) {
                key = new Key(
                        docKey.getUniversalID(),
                        docKey.getItemValueString("title"),
                        docKey.getItemValueString("description"),
                        docKey.getItemValueString("dbUser"),
                        docKey.getItemValueString("db"),
                        docKey.getItemValueString("view"),
                        docKey.getItemValueString("sep1"),
                        docKey.getItemValueString("sep2"),
                        docKey.getItemValueString("sep3"),
                        docKey.getItemValueString("sep4"),
                        docKey.getItemValueString("sep5")
                );

                if (!docKey.getItemValueString("field1Unid").isEmpty()) {
                    docField = db.getDocumentByUNID(docKey.getItemValueString("field1Unid"));
                    key.setField1(produceField(docField, viewRuleRef));
                }
                if (!docKey.getItemValueString("field2Unid").isEmpty()) {
                    docField = db.getDocumentByUNID(docKey.getItemValueString("field2Unid"));
                    key.setField2(produceField(docField, viewRuleRef));
                }
                if (!docKey.getItemValueString("field3Unid").isEmpty()) {
                    docField = db.getDocumentByUNID(docKey.getItemValueString("field3Unid"));
                    key.setField3(produceField(docField, viewRuleRef));
                }
                if (!docKey.getItemValueString("field4Unid").isEmpty()) {
                    docField = db.getDocumentByUNID(docKey.getItemValueString("field4Unid"));
                    key.setField4(produceField(docField, viewRuleRef));
                }
                if (!docKey.getItemValueString("field5Unid").isEmpty()) {
                    docField = db.getDocumentByUNID(docKey.getItemValueString("field5Unid"));
                    key.setField5(produceField(docField, viewRuleRef));
                }

                object.addKey(key);

                docKeyTmp = colKey.getNextDocument();
                docKey.recycle();
                docKey = docKeyTmp;
            }

            colField = viewFieldRef.getAllDocumentsByKey(docObject.getUniversalID(), true);
            docField = colField.getFirstDocument();
            while (docField != null) {
                object.addField(produceField(docField, viewRuleRef));

                docFieldTmp = colField.getNextDocument();
                docField.recycle();
                docField = docFieldTmp;
            }

        } catch (Exception e) {
            MyLog.add2Log(e);
        } finally {
            try {
                if (docKey != null) {
                    docKey.recycle();
                }
                if (docKeyTmp != null) {
                    docKeyTmp.recycle();
                }
                if (docField != null) {
                    docField.recycle();
                }
                if (docFieldTmp != null) {
                    docFieldTmp.recycle();
                }

                if (colKey != null) {
                    colKey.recycle();
                }
                if (colField != null) {
                    colField.recycle();
                }
            } catch (Exception e) {
                MyLog.add2Log(e);
            }
        }

        return object;
    }

    private Field produceField(Document docField, View viewRuleRef) {
        Field field = null;
        Rule rule;

        DocumentCollection colRule = null;

        Document docRule = null;
        Document docRuleTmp = null;

        try {
            field = new Field(
                    docField.getUniversalID(),
                    docField.getItemValueString("titleSys"),
                    docField.getItemValueString("titleUser"),
                    docField.getItemValueString("description"),
                    docField.getItemValueString("type"),
                    docField.getItemValueString("xlsCell"),
                    "1".equals(docField.getItemValueString("emptyFlag"))
            );

            colRule = viewRuleRef.getAllDocumentsByKey(docField.getUniversalID(), true);
            docRule = colRule.getFirstDocument();
            while (docRule != null) {
                rule = new Rule(
                        docRule.getUniversalID(),
                        docRule.getItemValueString("title"),
                        docRule.getItemValueString("description"),
                        docRule.getItemValueInteger("number"),
                        docRule.getItemValueString("type"),
                        docRule.getItemValueString("formula"),
                        docRule.getItemValueString("robot")
                );
                field.addRule(rule);

                docRuleTmp = colRule.getNextDocument();
                docRule.recycle();
                docRule = docRuleTmp;
            }

        } catch (Exception e) {
            MyLog.add2Log(e);
        } finally {
            try {
                if (docRule != null) {
                    docRule.recycle();
                }
                if (docRuleTmp != null) {
                    docRuleTmp.recycle();
                }
                if (colRule != null) {
                    colRule.recycle();
                }
            } catch (Exception e) {
                MyLog.add2Log(e);
            }
        }

        return field;
    }
}
