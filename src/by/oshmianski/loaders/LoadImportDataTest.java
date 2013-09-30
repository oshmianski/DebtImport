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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

public class LoadImportDataTest implements Runnable, Loader {
    private boolean executed = false;
    private boolean canceled = false;

    /**
     * UI callback
     */
    protected UIProcessor ui;

    private String moduleName;

    public LoadImportDataTest(UIProcessor ui) {
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
        OPCPackage pkg = null;
        XSSFWorkbook wb = null;

        try {
            NotesThread.sinitThread();
            session = NotesFactory.createSession();
            db = session.getDatabase(null, null);

            String filePath = ui.getFileField().getText();

            TemplateImport templateImport = ui.getTemplateImport();

            File file = new File(filePath);
            if (file == null)
                throw new CantOpenFileException("Не удалось открыть файл: " + filePath);

            // XSSFWorkbook, File
            pkg = OPCPackage.open(file);
            wb = new XSSFWorkbook(pkg);

            Sheet sheet1 = wb.getSheetAt(0);

            int count = sheet1.getLastRowNum();
            int start = ui.getStartFrom();
            if (start < 0)
                start = 0;
            else
                start = start - 1;

            ui.setCountAll2Import(count + 1 - start);
            ui.progressSetValue(0);
            ui.progressSetMaximum(count + 1 - start);

            DataMainItem dataMainItem;
            ArrayList<DataChildItem> dataChildItems = null;
            ArrayList<RecordObject> rObjects = null;
            ArrayList<RecordObjectField> rFields = null;
            DataChildItem dataChildItem;
            RecordObject rObject;
            RecordObjectField rField;
            String evalValue;
            String cellValue;
            boolean isRule;
            Row row;
            StringBuffer sb = new StringBuffer();
            SortedList<Rule> ruleSortedList;
            Vector v;

            int col2Description = ui.getCol2Description().getText().isEmpty() ? -1 : CellReference.convertColStringToIndex(ui.getCol2Description().getText());

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
                    rObject = new RecordObject(obj.getFormName());
                    rObjects.add(rObject);
                    rFields = new ArrayList<RecordObjectField>();

                    for (Field field : obj.getFields()) {
                        isRule = false;
                        evalValue = "";
                        cellValue = "";
                        Status status = Status.OK;

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

                            cellValue = field.getXmlCell().isEmpty() ? "" : getCellString(wb, row.getCell(CellReference.convertColStringToIndex(col)));

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
                            status = Status.ERROR;

                            dataMainItem.setStatus(Status.ERROR);

                            cellValue = ex.toString();
                        }

                        dataChildItem = new DataChildItem(
                                status,
                                obj.getTitle() + " [" + obj.getFormName() + "]",
                                "ячейка " + field.getXmlCell() + ", " + field.getTitleUser() + " [" + field.getTitleSys() + "]",
                                cellValue
                        );
                        dataChildItems.add(dataChildItem);
                        dataChildItem = null;

                        rField = new RecordObjectField(field.getTitleSys(), cellValue, RecordNodeFieldType.text);
                        rFields.add(rField);
                    }

                    rObject.setFields(rFields);

                    //TODO: определение уникалности конечного объекта
                }

                //TODO: обработка связей

                dataMainItem.setDataChildItems(dataChildItems);
                dataMainItem.setObjects(rObjects);

                ui.appendDataImport(dataMainItem);
                dataMainItem = null;

                ui.progressSetValue(i + 1);

                Thread.sleep(4);

                if (canceled) break;
                i++;
            }

//            dataChildItems.clear();
//            dataChildItems = null;

        } catch (Exception e) {
            MyLog.add2Log(e);
        } finally {
            try {
                if (pkg != null) {
                    pkg.close();
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

    /**
     * My custom exception class.
     */
    private static class CantOpenFileException extends Exception {
        public CantOpenFileException(String message) {
            super(message);
        }
    }
}
