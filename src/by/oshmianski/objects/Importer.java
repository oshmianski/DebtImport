package by.oshmianski.objects;

import by.oshmianski.loaders.LoadImportData;
import by.oshmianski.utils.MyLog;
import ca.odell.glazedlists.EventList;
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

/**
 * Created with IntelliJ IDEA.
 * User: VintsalovichS
 * Date: 02.10.13
 * Time: 10:40
 */
public class Importer {
    private LoadImportData loader;

    public Importer(LoadImportData loader) {
        this.loader = loader;
    }

    public void process() {
        if (loader.isTest())
            test();
        else
            work();
    }

    private void work() {
        preWork();

        loader.getUi().setProgressLabelText("Чтение и компоновка данных...");
        startTest();
        loader.getUi().setProgressLabelText("Чтение и компоновка данных...OK");

        if (!loader.isCanceled()) {
            loader.getUi().setProgressLabelText("Импорт данных...");
            startImport();
            loader.getUi().setProgressLabelText("Импорт данных...ОК");
        }

        postWork();
    }

    private void preWork() {
        loader.getUi().clearDataImport();

        loader.getUi().setButtonTestStopEnable(false);
        loader.getUi().setButtonTestStartEnable(false);
        loader.getUi().setButtonImportStartEnable(false);
        loader.getUi().setButtonImportStopEnable(true);
    }

    private void postWork() {
        loader.getUi().setButtonTestStopEnable(false);
        loader.getUi().setButtonTestStartEnable(true);
        loader.getUi().setButtonImportStartEnable(true);
        loader.getUi().setButtonImportStopEnable(false);
    }

    private void test() {
        preTest();

        loader.getUi().setProgressLabelText("Чтение и компоновка данных...");
        startTest();
        loader.getUi().setProgressLabelText("Чтение и компоновка данных...OK");

        postTest();
    }

    public void preTest() {
        loader.getUi().clearDataImport();

        loader.getUi().setButtonTestStopEnable(true);
        loader.getUi().setButtonTestStartEnable(false);
        loader.getUi().setButtonImportStartEnable(false);
        loader.getUi().setButtonImportStopEnable(false);
    }

    public void postTest() {
        loader.getUi().setButtonTestStopEnable(false);
        loader.getUi().setButtonTestStartEnable(true);
        loader.getUi().setButtonImportStartEnable(true);
        loader.getUi().setButtonImportStopEnable(false);
    }

    private void startImport() {
        Session session = null;
        Database db = null;
        View view = null;
        ViewNavigator nav = null;
        ViewEntry ve = null;
        ViewEntry vetmp = null;
        Map<String, Database> dbMap = new HashMap<String, Database>();
        Map<String, View> viewMap = new HashMap<String, View>();

        try {
            NotesThread.sinitThread();
            session = NotesFactory.createSession();
            db = session.getDatabase(null, null);

            EventList<DataMainItem> items = loader.getUi().getDataMainItems();

            loader.getUi().setProgressValue(0);
            loader.getUi().setProgressMaximum(items.size());

            int i = 0;
            for (DataMainItem dataMainItem : items) {



                Thread.sleep(2);
                i++;

                loader.getUi().countIncImported();
                loader.getUi().setProgressValue(i + 1);
            }

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

                if (!viewMap.isEmpty()) {
                    Iterator i = viewMap.entrySet().iterator();
                    while (i.hasNext()) {
                        Map.Entry entry = (Map.Entry) i.next();
                        ((View) entry.getValue()).recycle();
                    }
                    viewMap.clear();
                }

                if (!dbMap.isEmpty()) {
                    Iterator i = dbMap.entrySet().iterator();
                    while (i.hasNext()) {
                        Map.Entry entry = (Map.Entry) i.next();
                        ((Database) entry.getValue()).recycle();
                    }
                    dbMap.clear();
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

    private void startTest() {
        Session session = null;
        Database db = null;
        View view = null;
        ViewNavigator nav = null;
        ViewEntry ve = null;
        ViewEntry vetmp = null;
        OPCPackage pkg = null;
        XSSFWorkbook wb = null;
        Map<String, Database> dbMap = new HashMap<String, Database>();
        Map<String, View> viewMap = new HashMap<String, View>();
        Map<String, RecordObject> recordObjectMap = new HashMap<String, RecordObject>();

        try {
            NotesThread.sinitThread();
            session = NotesFactory.createSession();
            db = session.getDatabase(null, null);

            String filePath = loader.getUi().getFileField().getText();

            TemplateImport templateImport = loader.getUi().getTemplateImport();

            File file = new File(filePath);
            if (file == null)
                throw new CantOpenFileException("Не удалось открыть файл: " + filePath);

            // XSSFWorkbook, File
            pkg = OPCPackage.open(file);
            wb = new XSSFWorkbook(pkg);

            Sheet sheet1 = wb.getSheetAt(0);

            int count = sheet1.getLastRowNum();
            int start = loader.getUi().getStartFrom();
            if (start < 0)
                start = 0;
            else
                start = start - 1;

            loader.getUi().setCountAll2Import(count + 1 - start);
            loader.getUi().setProgressValue(0);
            loader.getUi().setProgressMaximum(count + 1 - start);

            DataMainItem dataMainItem;
            ArrayList<DataChildItem> dataChildItems = null;
            ArrayList<RecordObject> rObjects = null;
            ArrayList<RecordObjectField> rFields = null;
            DataChildItem dataChildItem;
            RecordObject rObject;
            RecordObjectField rField;
            String evalValue;
            String cellValue;
            String cellValueReal;
            boolean isRule;
            Row row;
            StringBuffer sb = new StringBuffer();
            SortedList<Rule> ruleSortedList;
            Vector v;

            int col2Description = loader.getUi().getCol2Description().getText().isEmpty() ? -1 : CellReference.convertColStringToIndex(loader.getUi().getCol2Description().getText());

            int i = start;

            Iterator<Row> it = sheet1.iterator();

            if (start > 0)
                for (int j = 0; j < start; j++)
                    if (it.hasNext())
                        it.next();

            while (it.hasNext()) {
                row = it.next();

                dataMainItem = new DataMainItem(i + 1, Status.OK, col2Description == -1 ? "" : getCellString(wb, row.getCell(col2Description)));
                dataChildItems = new ArrayList<DataChildItem>();
                rObjects = new ArrayList<RecordObject>();

                for (Object obj : templateImport.getObjects()) {
                    String objTitle = obj.getTitle() + " [" + obj.getFormName() + "]";

                    rObject = new RecordObject(obj.getFormName());
                    rObjects.add(rObject);
                    rFields = new ArrayList<RecordObjectField>();

                    for (Field field : obj.getFields()) {
                        isRule = false;
                        evalValue = "";
                        cellValue = "";
                        cellValueReal = "";
                        String fieldTitle = "ячейка " + field.getXmlCell() + ", " + field.getTitleUser() + " [" + field.getTitleSys() + "]";

                        try {
                            String colStr = field.getXmlCell();
                            String col = "";
                            if ("@".equals(colStr.substring(0, 1))) {
                                String colStrArray[] = StringUtils.substringsBetween(colStr, "<", ">");
                                for (String str : colStrArray) {
                                    colStr = colStr.replaceAll("\\<" + str + "\\>", "\"" + row.getCell(CellReference.convertColStringToIndex(str)) + "\"").replaceAll("null", "");
                                }

                                Vector vec = session.evaluate(colStr);
                                col = vec.get(0).toString();
                            } else {
                                col = colStr;
                            }

                            cellValueReal = field.getXmlCell().isEmpty() ? "" : getCellString(wb, row.getCell(CellReference.convertColStringToIndex(col)));
                            cellValue = cellValueReal;

                            ruleSortedList = new SortedList<Rule>(field.getRules(), GlazedLists.chainComparators(GlazedLists.beanPropertyComparator(Rule.class, "number")));
                            for (Rule rule : ruleSortedList) {
                                if ("1".equals(rule.getType())) {
                                    v = session.evaluate(rule.getFormula().replaceAll("%value%", isRule ? evalValue : cellValue));
                                    for (int vindex = 0; vindex < v.size(); vindex++)
                                        sb.append(v.get(vindex));

                                    evalValue = sb.toString();
                                    sb.setLength(0);
                                    v.clear();
                                    v = null;
                                } else {
                                    evalValue = cellValue;
                                    //TODO: обработка роботов
                                }

                                isRule = true;
                            }
                            ruleSortedList.dispose();

                            cellValue = isRule ? evalValue : cellValue;
                        } catch (Exception ex) {
                            MyLog.add2Log(ex);

                            dataChildItem = new DataChildItem(
                                    Status.ERROR,
                                    objTitle,
                                    fieldTitle,
                                    ex.toString()
                            );
                            dataChildItems.add(dataChildItem);
                        }

                        if (cellValueReal.isEmpty() && field.isEmptyFlag()) {
                            dataChildItem = new DataChildItem(
                                    Status.WARNING,
                                    objTitle,
                                    fieldTitle,
                                    "Значение ячейки пустое, объект создан не будет"
                            );
                            dataChildItems.add(dataChildItem);
                            rObject.setFlagEmpty(true);
                        }

                        rField = new RecordObjectField(field.getTitleSys(), cellValueReal.isEmpty() ? cellValueReal : cellValue, RecordNodeFieldType.text);
                        rFields.add(rField);
                    }

                    rObject.setFields(rFields);

                    //проверка на уникальность
                    if (!rObject.isFlagEmpty()) {
                        StringBuilder keyStr = new StringBuilder();
                        RecordObjectField recordObjectField;
                        for (Key key : obj.getKeys()) {
                            if (key.getField1() != null) {
                                recordObjectField = rObject.getFieldByTitle(key.getField1().getTitleSys());
                                keyStr.append(key.getPref1());
                                keyStr.append(recordObjectField.getValue());
                            }
                            if (key.getField2() != null) {
                                recordObjectField = rObject.getFieldByTitle(key.getField2().getTitleSys());
                                keyStr.append(key.getPref2());
                                keyStr.append(recordObjectField.getValue());
                            }
                            if (key.getField3() != null) {
                                recordObjectField = rObject.getFieldByTitle(key.getField3().getTitleSys());
                                keyStr.append(key.getPref3());
                                keyStr.append(recordObjectField.getValue());
                            }
                            if (key.getField4() != null) {
                                recordObjectField = rObject.getFieldByTitle(key.getField4().getTitleSys());
                                keyStr.append(key.getPref4());
                                keyStr.append(recordObjectField.getValue());
                            }
                            if (key.getField5() != null) {
                                recordObjectField = rObject.getFieldByTitle(key.getField5().getTitleSys());
                                keyStr.append(key.getPref5());
                                keyStr.append(recordObjectField.getValue());
                            }
                            if (!dbMap.containsKey(key.getDb())) {
                                Database database = session.getDatabase(null, null);
                                database.openByReplicaID(session.getServerName() == null ? "" : session.getServerName(), key.getDb());
                                dbMap.put(key.getDb(), database);
                            }

                            if (!viewMap.containsKey(key.getView())) {
                                viewMap.put(key.getView(), dbMap.get(key.getDb()).getView(key.getView()));
                            }

                            DocumentCollection col = null;
                            try {
                                col = viewMap.get(key.getView()).getAllDocumentsByKey(keyStr.toString(), true);
                                if (col.getCount() > 0) {
                                    dataChildItem = new DataChildItem(
                                            Status.WARNING,
                                            objTitle,
                                            keyStr.toString(),
                                            "Объект уже существует в базе данных"
                                    );
                                    dataChildItems.add(dataChildItem);
                                    rObject.setExistInDB(true);
                                    Document document = col.getFirstDocument();
                                    rObject.setLinkKey(document.getUniversalID());
                                    document.recycle();
                                } else {
                                    if (recordObjectMap.containsKey(keyStr.toString())) {
                                        rObject.setExistInPrevios(true);
                                        rObject.setLinkKey(keyStr.toString());

                                        dataChildItem = new DataChildItem(
                                                Status.WARNING,
                                                objTitle,
                                                keyStr.toString(),
                                                "Объект уже существует среди предыдущих импортируемых"
                                        );
                                        dataChildItems.add(dataChildItem);
                                    } else {
                                        recordObjectMap.put(keyStr.toString(), rObject);
                                    }
                                }
                            } finally {
                                if (col != null)
                                    col.recycle();
                            }

                            keyStr.setLength(0);
                        }
                    }
                }

                dataMainItem.setDataChildItems(dataChildItems);
                dataMainItem.setObjects(rObjects);

                try {
                    //обработка связей
                    for (Link link : templateImport.getLinks()) {
                        if ("1".equals(link.getType())) {//связь один-ко-многим
                            Object childObject = link.getChildObject();
                            RecordObject childRecordObject = dataMainItem.getRecordObjectByTitle(childObject.getFormName());

                            if (!(childRecordObject.isFlagEmpty() || childRecordObject.isExistInDB() || childRecordObject.isExistInPrevios())) {
                                Object mainObject = link.getMainObject();
                                RecordObject mainRecordObject = dataMainItem.getRecordObjectByTitle(mainObject.getFormName());

                                while (mainRecordObject.isFlagEmpty()) {
                                    Link link1 = templateImport.getLinkByChildTitle(mainRecordObject.getTitle());
                                    mainRecordObject = dataMainItem.getRecordObjectByTitle(link1.getMainObject().getFormName());
                                }

                                childRecordObject.setMainObject(mainRecordObject);
                            }
                        } else {
                            //TODO: бработка связи много-ко-многим
                        }
                    }
                } catch (Exception ex) {
                    MyLog.add2Log(ex);
                    dataChildItem = new DataChildItem(
                            Status.ERROR,
                            "Формирование связей",
                            "Ошибка",
                            ex.toString()
                    );
                    dataMainItem.addDataChildItem(dataChildItem);
                }

                loader.getUi().appendDataImport(dataMainItem);

                loader.getUi().setProgressValue(i + 1);

                Thread.sleep(3);

                if (loader.isCanceled()) break;
                i++;
            }

        } catch (Exception e) {
            MyLog.add2Log(e);
        } finally {
            try {
                if (pkg != null) {
                    pkg.close();
                }

                if (!recordObjectMap.isEmpty()) {
                    recordObjectMap.clear();
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

                if (!viewMap.isEmpty()) {
                    Iterator i = viewMap.entrySet().iterator();
                    while (i.hasNext()) {
                        Map.Entry entry = (Map.Entry) i.next();
                        ((View) entry.getValue()).recycle();
                    }
                    viewMap.clear();
                }

                if (!dbMap.isEmpty()) {
                    Iterator i = dbMap.entrySet().iterator();
                    while (i.hasNext()) {
                        Map.Entry entry = (Map.Entry) i.next();
                        ((Database) entry.getValue()).recycle();
                    }
                    dbMap.clear();
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

    /**
     * My custom exception class.
     */
    private static class CantOpenFileException extends Exception {
        public CantOpenFileException(String message) {
            super(message);
        }
    }

    public String getCellString(Workbook wb, Cell cell) {
        String retValue = "";
        FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

        if (cell == null) return "";

        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                retValue = cell.getRichStringCellValue().getString();
                break;
            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    retValue = cell.getDateCellValue().toString();
                } else {
                    retValue = String.valueOf(cell.getNumericCellValue());
                }
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                retValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_FORMULA:
                CellValue cellValue = evaluator.evaluate(cell);

                switch (cellValue.getCellType()) {
                    case Cell.CELL_TYPE_BOOLEAN:
                        retValue = String.valueOf(cellValue.getBooleanValue());
                        break;
                    case Cell.CELL_TYPE_NUMERIC:
                        retValue = String.valueOf(cellValue.getNumberValue());
                        break;
                    case Cell.CELL_TYPE_STRING:
                        retValue = cellValue.getStringValue();
                        break;
                    case Cell.CELL_TYPE_BLANK:
                        break;
                    case Cell.CELL_TYPE_ERROR:
                        break;

                    // CELL_TYPE_FORMULA will never happen
                    case Cell.CELL_TYPE_FORMULA:
                        break;
                }
                break;
            default:

        }

        return retValue;
    }
}
