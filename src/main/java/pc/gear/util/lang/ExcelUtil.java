package pc.gear.util.lang;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.ss.util.NumberToTextConverter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ExcelUtil {

    public static Map<String, Sheet> workbookToMap(Workbook workbook) {
        Map<String, Sheet> sheetMap = new HashMap<>();

        // Iterate through sheets in the workbook
        Iterator<Sheet> sheetIterator = workbook.sheetIterator();
        while (sheetIterator.hasNext()) {
            Sheet sheet = sheetIterator.next();
            sheetMap.put(sheet.getSheetName(), sheet);
        }

        return sheetMap;
    }

    public static int getColIndex(String colName) {
        return CellReference.convertColStringToIndex(colName);
    }


    public static String getDataFromCell(Sheet sheet, int rowIndex, int colIndex) {
        Row row = sheet.getRow(rowIndex);
        if (row != null) {
            Cell cell = row.getCell(colIndex);
            if (cell != null) {
                return getDataFromCell(cell);
            }
        }
        return null;
    }

    public static String getDataFromCell(Cell cell) {
        if (cell != null) {
            String context = switch (cell.getCellType()) {
                case NUMERIC -> NumberToTextConverter.toText(cell.getNumericCellValue());
                case STRING -> cell.getStringCellValue();
                case BOOLEAN -> Boolean.valueOf(cell.getBooleanCellValue()).toString();
                case FORMULA -> getDataFormulaCell(cell);
                default -> null;
            };
            return context;
        }
        return null;
    }

    public static String getDataFormulaCell(Cell cell) {
        String context = switch (cell.getCachedFormulaResultType()) {
            case NUMERIC -> NumberToTextConverter.toText(cell.getNumericCellValue());
            case STRING -> cell.getStringCellValue();
            case BOOLEAN -> Boolean.valueOf(cell.getBooleanCellValue()).toString();
            default -> null;
        };
        return context;
    }

}
