package by.oshmianski.test;

import by.oshmianski.utils.MyLog;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;

import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: VintsalovichS
 * Date: 9/12/13
 * Time: 4:08 PM
 */
public class XlsxReader {
    private Workbook wb;

    public XlsxReader() throws IOException, InvalidFormatException {
        wb = WorkbookFactory.create(new File("src/by/oshmianski/test/data/Sample.xlsx"));
        Sheet sheet1 = wb.getSheetAt(0);

        for (Row row : sheet1) {
            for (Cell cell : row) {
                CellReference cellRef = new CellReference(row.getRowNum(), cell.getColumnIndex());
                System.out.print(cellRef.formatAsString());
                System.out.print(" - ");

                System.out.println("cell string value = " + getCellString(cell));
            }
        }
    }

    public static void main(String[] args) {
        try {
            new XlsxReader();
        } catch (Exception e) {
            MyLog.add2Log(e);
        }
    }

    public String getCellString(Cell cell){
        String retValue = "";
        FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

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
