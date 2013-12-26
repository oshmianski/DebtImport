package by.oshmianski.objects;

import by.oshmianski.loaders.LoadImportData;
import by.oshmianski.utils.AppletParams;
import by.oshmianski.utils.MyLog;
import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.SortedList;
import lotus.domino.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.Color;
import java.io.File;
import java.text.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: VintsalovichS
 * Date: 02.10.13
 * Time: 10:40
 */
public class Importer {
    private LoadImportData loader;

    private Map<String, RecordObject> recordObjectMap = new HashMap<String, RecordObject>();
    private String importKey;
    private FuzzySearch fuzzySearchAddress;
    private final SimpleDateFormat formatterDateTime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private FormulaEvaluator evaluator = null;
    //    private DecimalFormat formatNumber = (DecimalFormat) DecimalFormat.getInstance();
    private DecimalFormat formatNumber = new DecimalFormat("0.######");
    private static final String MULTY_SEPARATOR = "-+||+-";

    public Importer(LoadImportData loader) {
        this.loader = loader;
        formatNumber.setGroupingUsed(false);
    }

    public void process() {
        if (loader.isTest())
            test();
        else
            work();

        if (!recordObjectMap.isEmpty()) {
            recordObjectMap.clear();
        }
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
        View view = null;
        ViewNavigator nav = null;
        ViewEntry ve = null;
        ViewEntry vetmp = null;
        Map<String, Database> dbMap = new HashMap<String, Database>();

        Database dbFI = null;
        Document noteFI = null;
        RichTextItem bodyFile = null;

        try {
            NotesThread.sinitThread();
            session = NotesFactory.createSession();

            TemplateImport templateImport = loader.getUi().getTemplateImport();

            if (templateImport.isCreateFI()) {
                Object importFact = templateImport.getImportFact();

                dbFI = session.getDatabase(null, null);
                dbFI.openByReplicaID(AppletParams.getInstance().getServer(), templateImport.getDbID());
                noteFI = dbFI.createDocument();
                noteFI.setUniversalID(importKey);
                noteFI.replaceItemValue("form", "ImportFact");

                if (importFact != null)
                    if (importFact.isComputeWithForm())
                        noteFI.computeWithForm(false, false);

                noteFI.replaceItemValue("fileName", loader.getUi().getFileField().getText());
                String path = loader.getUi().getFileField().getText();
                noteFI.replaceItemValue("fileNameShort", StringUtils.right(path, path.length() - path.lastIndexOf("\\") - 1));
                noteFI.replaceItemValue("TemplateImportTitle", templateImport.getTitle());
                bodyFile = noteFI.createRichTextItem("file");
                bodyFile.embedObject(EmbeddedObject.EMBED_ATTACHMENT, "", noteFI.getItemValueString("fileName").replaceAll("\\\\", "/"), "");

                if (importFact != null) {
                    noteFI.replaceItemValue(importFact.getUnidTitle(), noteFI.getUniversalID());
                } else {
                    noteFI.replaceItemValue("UNID", noteFI.getUniversalID());
                }

                if (importFact != null) {
                    if (importFact.isComputeWithForm())
                        noteFI.computeWithForm(false, false);

                    noteFI.replaceItemValue(importFact.getUnidTitle(), noteFI.getUniversalID());

                    Vector v;
                    StringBuilder sb = new StringBuilder();
                    String evalValue = "";

                    for (Field field : importFact.getFields()) {
                        evalValue = "";

                        for (Rule rule : field.getRules()) {
                            if ("1".equals(rule.getType())) {
                                v = session.evaluate(rule.getFormula(), noteFI);
                                for (int vindex = 0; vindex < v.size(); vindex++)
                                    sb.append(v.get(vindex));

                                evalValue = sb.toString();
                                sb.setLength(0);
                                v.clear();
                                v = null;
                            }
                        }

                        noteFI.replaceItemValue(field.getTitleSys(), evalValue);
                    }
                }
            }

            EventList<DataMainItem> items = loader.getUi().getDataMainItems();

            loader.getUi().setProgressValue(0);
            loader.getUi().setProgressMaximum(items.size());

            int i = 0;
            int j = 0;
            for (DataMainItem dataMainItem : items) {

                if (dataMainItem.getStatusFromChild() != Status.ERROR) {
                    Document document = null;
                    Document docParent = null;
                    Item item = null;
                    EventList<RecordObject> recordObjectEventList = null;
                    SortedList<RecordObject> recordObjectSortedList = null;
                    DateTime dateTime = null;

                    try {
                        recordObjectEventList = new BasicEventList<RecordObject>();
                        recordObjectEventList.addAll(dataMainItem.getObjects());
                        recordObjectSortedList = new SortedList<RecordObject>(recordObjectEventList, GlazedLists.beanPropertyComparator(RecordObject.class, "number"));
                        for (RecordObject rObject : recordObjectSortedList) {
                            if (!(rObject.isFlagEmpty() || rObject.isExistInDB() || rObject.isExistInPrevios())) {
                                Object object = loader.getUi().getTemplateImport().getObjectByFormName(rObject.getTitle());

                                if (!dbMap.containsKey(object.getDb())) {
                                    Database database = session.getDatabase(null, null);
                                    database.openByReplicaID(AppletParams.getInstance().getServer(), object.getDb());
                                    dbMap.put(object.getDb(), database);
                                }

                                document = dbMap.get(object.getDb()).createDocument();
                                document.replaceItemValue("form", object.getFormName());

                                if (rObject.isComputeWithForm()) {
                                    document.computeWithForm(false, false);
                                }

                                document.replaceItemValue(rObject.getUnidTitle(), document.getUniversalID());

                                for (RecordObjectField field : rObject.getFields()) {
                                    java.lang.Object val = null;

                                    if (!field.getValue().isEmpty()) {
                                        if (field.getType() == Field.TYPE.TEXT || field.getType() == Field.TYPE.AUTHORS || field.getType() == Field.TYPE.READERS) {
                                            val = field.isMultiple() ? new Vector(Arrays.asList(StringUtils.split(field.getValue(), MULTY_SEPARATOR))) : field.getValue();
                                        }

                                        if (field.getType() == Field.TYPE.NUMBER) {
                                            val = parseDecimal(field.getValue());
                                            //TODO: обработать мультизначность для чисео
                                        }

                                        if (field.getType() == Field.TYPE.DATETIME) {
                                            dateTime = session.createDateTime(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(field.getValue()));
                                            val = dateTime;
                                            //TODO: обработать мультизначность для дат
                                        }
                                    } else {
                                        val = "";
                                    }

                                    item = document.replaceItemValue(field.getTitle(), val);
                                    if (field.getType() == Field.TYPE.AUTHORS)
                                        item.setAuthors(true);
                                    if (field.getType() == Field.TYPE.READERS)
                                        item.setReaders(true);
                                }
                                for (RecordObject mainRecordObject : rObject.getMainObject()) {
                                    if (mainRecordObject != null) {
                                        while (mainRecordObject.isFlagEmpty()) {
                                            Link link1 = loader.getUi().getTemplateImport().getLinkByChildTitle(mainRecordObject.getTitle());
                                            mainRecordObject = dataMainItem.getRecordObjectByObjUnid(link1.getMainObject().getUnid());
                                        }

                                        while (mainRecordObject.isExistInPrevios() && !mainRecordObject.isExistInDB()) {
                                            mainRecordObject = recordObjectMap.get(mainRecordObject.getLinkKey());
                                        }

                                        if (!dbMap.containsKey(mainRecordObject.getDb())) {
                                            Database database = session.getDatabase(null, null);
                                            database.openByReplicaID(AppletParams.getInstance().getServer(), mainRecordObject.getDb());
                                            dbMap.put(mainRecordObject.getDb(), database);
                                        }
                                        docParent = dbMap.get(mainRecordObject.getDb()).getDocumentByUNID(mainRecordObject.getLinkKey());

                                        Link link = loader.getUi().getTemplateImport().getLinkByMainAndChildTitle(mainRecordObject.getTitle(), rObject.getTitle());
                                        switch (Integer.valueOf(link.getResponseField())) {
                                            case 1:
                                                if (link.isMakeResponse())
                                                    document.makeResponse(docParent);
                                                else
                                                    document.replaceItemValue("$REF", docParent.getUniversalID());

                                                break;
                                            case 2:
                                                if (link.isMakeResponse()) {
                                                    document.makeResponse(docParent);
                                                    document.copyItem(document.getFirstItem("$REF"), "$REF_" + link.getMainObject().getFormName());
                                                    document.removeItem("$REF");
                                                } else {
                                                    document.replaceItemValue("$REF_" + link.getMainObject().getFormName(), docParent.getUniversalID());
                                                }

                                                break;
                                            case 3:
                                                if (link.isMakeResponse()) {
                                                    document.makeResponse(docParent);
                                                    document.copyItem(document.getFirstItem("$REF"), link.getResponseFieldCustom());
                                                    document.removeItem("$REF");
                                                } else {
                                                    document.replaceItemValue(link.getResponseFieldCustom(), docParent.getUniversalID());
                                                }
                                                break;
                                            default:
                                                document.makeResponse(docParent);
                                        }

                                    }
                                }

                                rObject.setLinkKey(document.getUniversalID());

                                document.save();
                                loader.getUi().countIncImported();
                                j++;
                            }
                        }

                    } catch (Exception ex) {
                        MyLog.add2Log(ex);
                        DataChildItem dataChildItem = new DataChildItem(
                                Status.ERROR,
                                "_Ошибка",
                                "Импорт",
                                ex.toString()
                        );
                        dataMainItem.getDataChildItems().add(dataChildItem);
                    } finally {
                        if (recordObjectSortedList != null)
                            recordObjectSortedList.dispose();

                        if (recordObjectEventList != null)
                            recordObjectEventList.dispose();

                        if (dateTime != null)
                            dateTime.recycle();

                        if (document != null)
                            document.recycle();

                        if (docParent != null)
                            docParent.recycle();
                    }
                }

//                Thread.sleep(2);
                i++;

                loader.getUi().setProgressValue(i + 1);
            }

            if (templateImport.isCreateFI()) {
                if (j > 0) {
                    noteFI.replaceItemValue("importedObjectCount", j);
                    noteFI.save();
                } else {
                    Calendar now = Calendar.getInstance();
                    MyLog.add2Log(formatterDateTime.format(now.getTime()) + " Факт импорта не создан, т.к. ничего не импортировано!", true, new Color(0xC26802));
                }
            }


        } catch (Exception e) {
            MyLog.add2Log(e);
        } finally {
            try {
                if (bodyFile != null)
                    bodyFile.recycle();

                if (noteFI != null)
                    noteFI.recycle();

                if (dbFI != null)
                    dbFI.recycle();

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

                if (!dbMap.isEmpty()) {
                    Iterator i = dbMap.entrySet().iterator();
                    while (i.hasNext()) {
                        Map.Entry entry = (Map.Entry) i.next();
                        ((Database) entry.getValue()).recycle();
                    }
                    dbMap.clear();
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
        Database dbGEO = null;
        View view = null;
        View viewGEO = null;
        ViewNavigator nav = null;
        ViewEntry ve = null;
        ViewEntry vetmp = null;
        OPCPackage pkg = null;
        XSSFWorkbook wb = null;
        Map<String, Database> dbMap = new HashMap<String, Database>();
        Map<String, View> viewMap = new HashMap<String, View>();
        Database dbFI = null;
        Document noteFI = null;

        try {
            NotesThread.sinitThread();
            session = NotesFactory.createSession();

            String filePath = loader.getUi().getFileField().getText();

            TemplateImport templateImport = loader.getUi().getTemplateImport();

            db = session.getDatabase(null, null);
            db.openByReplicaID(AppletParams.getInstance().getServer(), templateImport.getDbID());
            dbGEO = session.getDatabase(null, null);
            dbGEO.openByReplicaID(AppletParams.getInstance().getServer(), AppletParams.getInstance().getDbGEO());
            viewGEO = dbGEO.getView(AppletParams.getInstance().getViewGEOCity());
            viewGEO.setAutoUpdate(false);

            File file = new File(filePath);
            if (file == null)
                throw new CantOpenFileException("Не удалось открыть файл: " + filePath);

            // XSSFWorkbook, File
            pkg = OPCPackage.open(file, PackageAccess.READ);
            wb = new XSSFWorkbook(pkg);

            Sheet sheet1 = wb.getSheetAt(0);

            int count = sheet1.getLastRowNum();
            int start = loader.getUi().getStartFrom();
            if (start < 0) {
                start = 0;
            } else {
                start = start - 1;
            }
            int end = loader.getUi().getEndTo();
            if (end == -1) {
                end = 1000000; //костыль, но по другому пока не знаю как
            } else {
                end--;
            }

            if (templateImport.isCreateFI()) {
                dbFI = session.getDatabase(null, null);
                dbFI.openByReplicaID(AppletParams.getInstance().getServer(), templateImport.getDbID());
                noteFI = dbFI.createDocument();
                importKey = noteFI.getUniversalID();
            } else {
                importKey = RandomStringUtils.random(32, true, true);
            }

            loader.getUi().setCountAll2Import(count + 1 - start);
            loader.getUi().setProgressValue(0);
            loader.getUi().setProgressMaximum(count + 1 - start);

            DataMainItem dataMainItem;
            ArrayList<DataChildItem> dataChildItems = null;
            ArrayList<RecordObject> rObjects = null;
            ArrayList<RecordObjectField> rFields = null;
            DataChildItem dataChildItem;
            RecordObject rObject;

            Row row;
            Row rowFirst;

            int col2Description = loader.getUi().getCol2Description().isEmpty() ? -1 : CellReference.convertColStringToIndex(loader.getUi().getCol2Description());

            int i = start;

            Iterator<Row> it = sheet1.iterator();

            if (start > 0)
                for (int j = 0; j < start; j++)
                    if (it.hasNext())
                        it.next();

            rowFirst = sheet1.getRow(0);

            while (it.hasNext() && i <= end) {
                row = it.next();

                dataMainItem = new DataMainItem(i + 1, Status.OK, col2Description == -1 ? "" : getCellString(wb, row.getCell(col2Description)), templateImport);
                dataChildItems = new ArrayList<DataChildItem>();
                rObjects = new ArrayList<RecordObject>();

                int j = 0;
                for (Cell cell : rowFirst) {
                    Cell cell1 = row.getCell(j);
                    //TODO: В следующей строчке плавающая ошибка: nullPointerException. нужно разобраться
                    String value = "";
                    if (loader.getUi().getCellHeaders().size() - 1 < j)
                        value = CellReference.convertNumToColString(j);
                    else
                        value = loader.getUi().getCellHeaders().get(j).toString();

                    String description = getCellString(wb, cell1);

                    dataChildItem = new DataChildItem(
                            Status.INFO,
                            "Данные",
                            value,
                            description);

                    dataChildItems.add(dataChildItem);

                    j++;
                }

                for (Object obj : templateImport.getObjects()) {
                    rObject = new RecordObject(obj.getUnid(), obj.getNumber(), obj.getUnidTitle(), obj.getFormName(), obj.getTitle(), obj.getDb(), obj.isComputeWithForm());
                    rObjects.add(rObject);
                    rFields = new ArrayList<RecordObjectField>();

                    try {
                        processFields(
                                session,
                                db,
                                viewGEO,
                                wb,
                                row,
                                obj,
                                dataChildItems,
                                rFields,
                                rObject);
                    } catch (Exception ex) {
                        MyLog.add2Log(ex);
                        dataChildItem = new DataChildItem(
                                Status.ERROR,
                                "_Заполение полей",
                                "Ошибка",
                                ex.toString()
                        );
                        dataChildItems.add(dataChildItem);
                    }

                    rObject.setFields(rFields);

                    try {
                        checkUnique(
                                session,
                                obj,
                                rObject,
                                dbMap,
                                viewMap,
                                recordObjectMap,
                                dataChildItems);
                    } catch (Exception ex) {
                        MyLog.add2Log(ex);
                        dataChildItem = new DataChildItem(
                                Status.ERROR,
                                "_Проверка уникальности",
                                "Ошибка",
                                ex.toString()
                        );
                        dataChildItems.add(dataChildItem);
                    }
                }

                dataMainItem.setDataChildItems(dataChildItems);
                dataMainItem.setObjects(rObjects);

                try {
                    //обработка связей
                    for (Link link : templateImport.getLinks()) {
                        if ("1".equals(link.getType())) {//связь один-ко-многим
                            Object childObject = link.getChildObject();
                            RecordObject childRecordObject = dataMainItem.getRecordObjectByObjUnid(childObject.getUnid());

                            if (!(childRecordObject.isFlagEmpty() || childRecordObject.isExistInDB() || childRecordObject.isExistInPrevios())) {
                                Object mainObject = link.getMainObject();
                                RecordObject mainRecordObject = dataMainItem.getRecordObjectByObjUnid(mainObject.getUnid());

                                while (mainRecordObject.isFlagEmpty()) {
                                    Link link1 = templateImport.getLinkByChildTitle(mainRecordObject.getTitle());
                                    mainRecordObject = dataMainItem.getRecordObjectByObjUnid(link1.getMainObject().getUnid());
                                }

                                childRecordObject.addMainObject(mainRecordObject);
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
                            "_Ошибка",
                            ex.toString()
                    );
                    dataMainItem.addDataChildItem(dataChildItem);
                }

                loader.getUi().appendDataImport(dataMainItem);

                loader.getUi().setProgressValue(i + 1);
                loader.getUi().setFilteredCount();

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

                if (noteFI != null)
                    noteFI.recycle();

                if (dbFI != null)
                    dbFI.recycle();

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
                if (viewGEO != null) {
                    viewGEO.recycle();
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
                if (dbGEO != null) {
                    dbGEO.recycle();
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
        if (evaluator == null)
            evaluator = wb.getCreationHelper().createFormulaEvaluator();

        if (cell == null) return "";

        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                retValue = cell.getRichStringCellValue().getString();
                break;
            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    retValue = formatterDateTime.format(cell.getDateCellValue());
                } else {
                    retValue = String.valueOf(formatNumber.format(cell.getNumericCellValue()));
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

    public CellField getCellField(Workbook wb, Cell cell) {
        CellField cellField = new CellField("", Field.TYPE.TEXT);

        if (evaluator == null)
            evaluator = wb.getCreationHelper().createFormulaEvaluator();

        if (cell == null) return cellField;

        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                cellField.setValue(cell.getStringCellValue());
                break;
            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    cellField.setValue(cell.getDateCellValue());
                    cellField.setType(Field.TYPE.DATETIME);
                } else {
                    cellField.setValue(String.valueOf(formatNumber.format(cell.getNumericCellValue())));
                    cellField.setType(Field.TYPE.NUMBER);
                }
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                cellField.setValue(String.valueOf(cell.getBooleanCellValue()));
                break;
            case Cell.CELL_TYPE_FORMULA:
                CellValue cellValue = evaluator.evaluate(cell);

                switch (cellValue.getCellType()) {
                    case Cell.CELL_TYPE_BOOLEAN:
                        cellField.setValue(String.valueOf(cellValue.getBooleanValue()));
                        break;
                    case Cell.CELL_TYPE_NUMERIC:
                        cellField.setValue(cellValue.getNumberValue());
                        cellField.setType(Field.TYPE.NUMBER);
                        break;
                    case Cell.CELL_TYPE_STRING:
                        cellField.setValue(cellValue.getStringValue());
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

        return cellField;
    }

    private void processFields(
            Session session,
            Database db,
            View viewGEO,
            XSSFWorkbook wb,
            Row row,
            Object obj,
            ArrayList<DataChildItem> dataChildItems,
            ArrayList<RecordObjectField> rFields,
            RecordObject rObject) throws Exception {

        Document document = null;
        RecordObjectField rField;
        String evalValue;
        String cellValue;
        String cellValueReal;
        boolean isRule;
        SortedList<Rule> ruleSortedList = null;
        SortedList<Field> fieldSortedList = null;
        Vector v;
        StringBuilder sb = new StringBuilder();

        try {
            document = db.createDocument();

            fieldSortedList = new SortedList<Field>(obj.getFieldsNotFake(), GlazedLists.chainComparators(GlazedLists.beanPropertyComparator(Field.class, "num")));

            for (Field field : fieldSortedList) {
                isRule = false;
                evalValue = "";
                cellValue = "";
                cellValueReal = "";
                String fieldTitle = "ячейка " + field.getXmlCell() + ", " + field.getTitleUser() + " [" + field.getTitleSys() + "]";

                try {
                    String colStr = field.getXmlCell();
                    String col = "";
                    if (!colStr.isEmpty()) {
                        if ("@".equals(colStr.substring(0, 1))) {
                            String colStrArray[] = StringUtils.substringsBetween(colStr, "<", ">");
                            for (String str : colStrArray) {
                                colStr = colStr.replaceAll("\\<" + str + "\\>", "\"" + row.getCell(CellReference.convertColStringToIndex(str)) + "\"").replaceAll("null", "");
                            }

                            Vector vec = session.evaluate(colStr.replaceAll("\\{", "\\{"));
                            col = vec.get(0).toString();
                        } else {
                            col = colStr;
                        }

                        cellValueReal = field.getXmlCell().isEmpty() ? "" : getCellString(wb, row.getCell(CellReference.convertColStringToIndex(col)));
                        cellValue = cellValueReal;
                    } else {
                        cellValueReal = "";
                        cellValue = "";
                    }

                    ruleSortedList = new SortedList<Rule>(field.getRules(), GlazedLists.chainComparators(GlazedLists.beanPropertyComparator(Rule.class, "number")));
                    for (Rule rule : ruleSortedList) {
                        if ("1".equals(rule.getType())) {
                            v = session.evaluate(rule.getFormula().replaceAll("%value%", isRule ? evalValue : cellValue), document);
                            for (int vindex = 0; vindex < v.size(); vindex++) {
                                if (sb.length() > 0) sb.append(MULTY_SEPARATOR);
                                sb.append(v.get(vindex));
                            }

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

                    cellValue = isRule ? evalValue : cellValue;
                    document.replaceItemValue(field.getTitleSys(), cellValue);
                } catch (Exception ex) {
                    MyLog.add2Log(ex);

                    DataChildItem dataChildItem = new DataChildItem(
                            Status.ERROR,
                            obj.getTitle() + " [" + obj.getFormName() + "]",
                            fieldTitle,
                            ex.toString()
                    );
                    dataChildItems.add(dataChildItem);
                } finally {
                    if (ruleSortedList != null)
                        ruleSortedList.dispose();
                }

                if (cellValueReal.isEmpty() && field.isEmptyFlag()) {
                    DataChildItem dataChildItem = new DataChildItem(
                            Status.WARNING_OBJECT_WILL_NOT_CREATE,
                            "_" + obj.getTitle() + " [" + obj.getFormName() + "]",
                            fieldTitle,
                            "Значение ячейки пустое, объект создан не будет"
                    );
                    dataChildItems.add(dataChildItem);
                    rObject.setFlagEmpty(true);
                }

                if ("#ADDRESS".equalsIgnoreCase(field.getTitleSys())) {
                    if (fuzzySearchAddress == null)
                        fuzzySearchAddress = new FuzzySearch(viewGEO);
                    Address address = fuzzySearchAddress.getAddress(cellValue, dataChildItems);

                    fillRecordObjectFieldsAddress(rFields, address);
                } else if ("#ADDRESS_STRUCTURED_1".equalsIgnoreCase(field.getTitleSys())) {
                    if (fuzzySearchAddress == null)
                        fuzzySearchAddress = new FuzzySearch(viewGEO);
                    Address address = fuzzySearchAddress.getAddressStructured1(cellValue, dataChildItems);

                    fillRecordObjectFieldsAddress(rFields, address);
                } else if ("#PASSPORT".equalsIgnoreCase(field.getTitleSys())) {
                    if (fuzzySearchAddress == null)
                        fuzzySearchAddress = new FuzzySearch(viewGEO);
                    Passport passport = fuzzySearchAddress.getPassport(cellValue, dataChildItems);

                    fillRecordObjectFieldsPassport(rFields, passport);
                } else {
                    if (cellValue.isEmpty() && field.isEmptyFlagSignal()) {
                        DataChildItem dataChildItem = new DataChildItem(
                                Status.WARNING_EMPTY_FIELD,
                                "_" + obj.getTitle() + " [" + obj.getFormName() + "]",
                                fieldTitle,
                                "Значение ячейки пустое"
                        );
                        dataChildItems.add(dataChildItem);
                    }

                    rField = new RecordObjectField(field.getTitleSys(), field.getTitleUser(), cellValue, field.getType(), field.isMultiple());
                    rFields.add(rField);
                }
            }
        } catch (Exception ex) {
            MyLog.add2Log(ex);

            DataChildItem dataChildItem = new DataChildItem(
                    Status.ERROR,
                    obj.getTitle() + " [" + obj.getFormName() + "]",
                    "Ошибка работы с документом",
                    ex.toString()
            );
            dataChildItems.add(dataChildItem);
        } finally {
            if (fieldSortedList != null)
                fieldSortedList.dispose();

            if (document != null) {
                document.recycle();
            }
        }

        rField = new RecordObjectField("UNIDIF", "", importKey, Field.TYPE.TEXT, false);
        rFields.add(rField);
    }

    private void fillRecordObjectFieldsPassport(ArrayList<RecordObjectField> rFields, Passport passport) {
        RecordObjectField rField;

        rField = new RecordObjectField("passType", "Тип", passport.getPassType(), Field.TYPE.TEXT, false);
        rFields.add(rField);

        rField = new RecordObjectField("passNum", "Номер", passport.getPassNum(), Field.TYPE.TEXT, false);
        rFields.add(rField);

        rField = new RecordObjectField("passDate", "Дата выдачи", passport.getPassDate(), Field.TYPE.DATETIME, false);
        rFields.add(rField);

        rField = new RecordObjectField("passOrg", "Организация", passport.getPassOrg(), Field.TYPE.TEXT, false);
        rFields.add(rField);
    }

    private void fillRecordObjectFieldsAddress(ArrayList<RecordObjectField> rFields, Address address) {
        RecordObjectField rField;

        rField = new RecordObjectField("index", "Индекс", address.getIndex(), Field.TYPE.TEXT, false);
        rFields.add(rField);

        rField = new RecordObjectField("country", "Страна", address.getCountry(), Field.TYPE.TEXT, false);
        rFields.add(rField);

        rField = new RecordObjectField("region", "Область", address.getRegion(), Field.TYPE.TEXT, false);
        rFields.add(rField);

        rField = new RecordObjectField("district", "Район", address.getDistrict(), Field.TYPE.TEXT, false);
        rFields.add(rField);

        rField = new RecordObjectField("city", "Нас. пункт", address.getCity(), Field.TYPE.TEXT, false);
        rFields.add(rField);

        rField = new RecordObjectField("cityType", "Тип нас. пункта", address.getCityType(), Field.TYPE.TEXT, false);
        rFields.add(rField);

        rField = new RecordObjectField("street", "Улица", address.getStreet(), Field.TYPE.TEXT, false);
        rFields.add(rField);

        rField = new RecordObjectField("streetType", "Тип улицы", address.getStreetType(), Field.TYPE.TEXT, false);
        rFields.add(rField);

        rField = new RecordObjectField("house", "Дом", address.getHouse(), Field.TYPE.TEXT, false);
        rFields.add(rField);

        rField = new RecordObjectField("flat", "Квартира", address.getFlat(), Field.TYPE.TEXT, false);
        rFields.add(rField);
    }

    private void checkUnique(
            Session session,
            Object obj,
            RecordObject rObject,
            Map<String, Database> dbMap,
            Map<String, View> viewMap,
            Map<String, RecordObject> recordObjectMap,
            ArrayList<DataChildItem> dataChildItems) throws Exception {

        //проверка на уникальность
        if (!rObject.isFlagEmpty()) {
            StringBuilder keyStr = new StringBuilder();
            RecordObjectField recordObjectField;
            for (Key key : obj.getKeys()) {
                if (key.getField1() != null) {
                    recordObjectField = rObject.getFieldByTitle(key.getField1().getTitleSys());
                    keyStr.append(key.getPref1());
                    keyStr.append(recordObjectField == null ? "" : recordObjectField.getValue());
                }
                if (key.getField2() != null) {
                    recordObjectField = rObject.getFieldByTitle(key.getField2().getTitleSys());
                    keyStr.append(key.getPref2());
                    keyStr.append(recordObjectField == null ? "" : recordObjectField.getValue());
                }
                if (key.getField3() != null) {
                    recordObjectField = rObject.getFieldByTitle(key.getField3().getTitleSys());
                    keyStr.append(key.getPref3());
                    keyStr.append(recordObjectField == null ? "" : recordObjectField.getValue());
                }
                if (key.getField4() != null) {
                    recordObjectField = rObject.getFieldByTitle(key.getField4().getTitleSys());
                    keyStr.append(key.getPref4());
                    keyStr.append(recordObjectField == null ? "" : recordObjectField.getValue());
                }
                if (key.getField5() != null) {
                    recordObjectField = rObject.getFieldByTitle(key.getField5().getTitleSys());
                    keyStr.append(key.getPref5());
                    keyStr.append(recordObjectField == null ? "" : recordObjectField.getValue());
                }
                if (!dbMap.containsKey(key.getDb())) {
                    Database database = session.getDatabase(null, null);
                    database.openByReplicaID(AppletParams.getInstance().getServer(), key.getDb());
                    dbMap.put(key.getDb(), database);
                }

                if (!viewMap.containsKey(key.getView())) {
                    viewMap.put(key.getView(), dbMap.get(key.getDb()).getView(key.getView()));
                }

                DocumentCollection col = null;
                Document document = null;
                try {
                    col = viewMap.get(key.getView()).getAllDocumentsByKey(keyStr.toString(), true);
                    if (col.getCount() > 0) {
                        DataChildItem dataChildItem = new DataChildItem(
                                Status.WARNING_ALREADY_EXIST_IN_DB,
                                "_" + obj.getTitle() + " [" + obj.getFormName() + "]",
                                keyStr.toString(),
                                "Объект уже существует в базе данных"
                        );
                        dataChildItems.add(dataChildItem);
                        rObject.setExistInDB(true);
                        document = col.getFirstDocument();
                        rObject.setLinkKey(document.getUniversalID());
                        document.recycle();
                    } else {
                        if (recordObjectMap.containsKey(keyStr.toString())) {
                            rObject.setExistInPrevios(true);
                            rObject.setLinkKey(keyStr.toString());

                            DataChildItem dataChildItem = new DataChildItem(
                                    Status.WARNING_ALREADY_EXIST_IN_PREVIOUS,
                                    "_" + obj.getTitle() + " [" + obj.getFormName() + "]",
                                    keyStr.toString(),
                                    "Объект уже существует среди предыдущих импортируемых"
                            );
                            dataChildItems.add(dataChildItem);
                        } else {
                            recordObjectMap.put(keyStr.toString(), rObject);
                        }
                    }
                } finally {
                    if (document != null)
                        document.recycle();

                    if (col != null)
                        col.recycle();
                }

                keyStr.setLength(0);
            }
        }
    }

    public double parseDecimal(String input) throws NullPointerException, ParseException {
        if (input == null) {
            throw new NullPointerException();
        }

        input = input.trim();

        DecimalFormat format = (DecimalFormat) DecimalFormat.getInstance();
        DecimalFormatSymbols symbols = format.getDecimalFormatSymbols();
        String sep = symbols.getDecimalSeparator() + "";
        if (".".equals(sep)) {
            input = input.replaceAll(",", sep);
        }
        if (",".equals(sep)) {
            input = input.replaceAll("\\.", sep);
        }

        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        ParsePosition parsePosition = new ParsePosition(0);
        Number number = numberFormat.parse(input, parsePosition);

        if (parsePosition.getIndex() != input.length()) {
            throw new ParseException("Invalid input. Value = " + input, parsePosition.getIndex());
        }

        return number.doubleValue();
    }
}
