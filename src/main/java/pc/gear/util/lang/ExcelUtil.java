package pc.gear.util.lang;

import lombok.val;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.ss.util.NumberToTextConverter;
import pc.gear.annotaion.interfaces.ExcelColumn;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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

    public static void setCellValue(Sheet sheet, int rowIndex, int colIndex, Object value) {
        if (value != null) {
            if (sheet != null) {
                Row row = sheet.getRow(rowIndex);
                if (row != null) {
                    Cell cell = row.getCell(colIndex);
                    setCellValue(cell, value);
                }
            }
        }
    }

    public static void setCellValue(Cell cell, Object value) {
        String valueString = value.toString();
        if (cell != null) {
            if (NumberUtils.isCreatable(valueString)) {
                cell.setCellValue(Double.parseDouble(valueString));
            } else {
                // String value
                cell.setCellValue(valueString);
            }
        }
    }

    public static <T> void writeDataFromListObject(Sheet sheet, int sourceRowIndex, List<T> list) throws IllegalAccessException {
        Row sourceRow = sheet.getRow(sourceRowIndex);
        int index = 1;
        for (T item : list) {
            ExcelUtil.createRowAndCopyFromSourceRow(sheet, sourceRowIndex, index, sourceRow);
            // set value to cell
            for (Field field : item.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(ExcelColumn.class)) {
                    ExcelColumn ec = field.getAnnotation(ExcelColumn.class);
                    field.setAccessible(true);
                    Object val = field.get(item);
                    ExcelUtil.setCellValue(sheet, sourceRowIndex + index, ExcelUtil.getColIndex(ec.columnName()), val);
                }
            }
            ExcelUtil.setCellValue(sheet, sourceRowIndex + index, ExcelUtil.getColIndex("D"), index);
            index++;
        }
        // Delete source row
        // Handle merged regions before removing the row
        for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
            CellRangeAddress mergedRegion = sheet.getMergedRegion(i);
            if (mergedRegion.getFirstRow() <= sourceRowIndex && mergedRegion.getLastRow() >= sourceRowIndex) {
                // Remove merged region if it intersects with the row to be removed
                sheet.removeMergedRegion(i);
                i--; // Adjust the loop counter because the list of merged regions has changed
            }
        }
        sheet.removeRow(sourceRow);
        sheet.shiftRows(sourceRowIndex + 1, sheet.getLastRowNum(), -1, true, false);
    }

    public static void createRowAndCopyFromSourceRow(Sheet sheet, int sourceRowIndex, int index, Row sourceRow) {
        sheet.shiftRows(sourceRowIndex + index, sheet.getLastRowNum(), 1, true, false);
        Row targetRow = sheet.createRow(sourceRowIndex + index);
        // Copy cells from the source row to the target row
        for (int i = 0; i < sourceRow.getLastCellNum(); i++) {
            Cell sourceCell = sourceRow.getCell(i);
            Cell targetCell = targetRow.createCell(i);

            if (sourceCell != null) {
                // Set the cell value and style
                targetCell.setCellValue(sourceCell.getStringCellValue());
                targetCell.setCellStyle(sourceCell.getCellStyle());
            }

            // Handle merged cells
            for (int j = 0; j < sheet.getNumMergedRegions(); j++) {
                CellRangeAddress mergedRegion = sheet.getMergedRegion(j);
                if (mergedRegion.isInRange(sourceRowIndex, i)) {
                    boolean alreadyMerged = false;
                    for (int k = 0; k < sheet.getNumMergedRegions(); k++) {
                        CellRangeAddress existingMergedRegion = sheet.getMergedRegion(k);
                        if (existingMergedRegion.isInRange(sourceRowIndex + index, i)) {
                            alreadyMerged = true;
                            break;
                        }
                    }
                    if (!alreadyMerged) {
                        CellRangeAddress newMergedRegion = new CellRangeAddress(
                                sourceRowIndex + index,
                                sourceRowIndex + index + (mergedRegion.getLastRow() - mergedRegion.getFirstRow()),
                                mergedRegion.getFirstColumn(),
                                mergedRegion.getLastColumn()
                        );
                        sheet.addMergedRegion(newMergedRegion);
                    }
                    break;
                }
            }
        }
    }
}
