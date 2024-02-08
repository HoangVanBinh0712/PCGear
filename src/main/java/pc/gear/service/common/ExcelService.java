package pc.gear.service.common;

import org.apache.poi.ss.usermodel.Sheet;
import pc.gear.annotaion.interfaces.ExcelColumn;
import pc.gear.dto.excel.ImportProductDto;
import pc.gear.entity.Category;
import pc.gear.entity.Product;
import pc.gear.util.response.ApiError;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public interface ExcelService {

    void readObject(Map<String, Sheet> sheetMap, Object bean, int rowIndex, List<ApiError> errors) throws IllegalAccessException;

    void readObjectImportProduct(Map<String, Sheet> sheetMap, ImportProductDto bean, int rowIndex, List<ApiError> errors, List<Category> categories) throws IllegalAccessException;

    void readField(Map<String, Sheet> sheetMap, Object bean, Field field, int rowIndex, List<ApiError> errors) throws IllegalAccessException;

    void checkNumber(ExcelColumn excelColumn, Object bean, Field field, String value, int rowIndex, List<ApiError> errors) throws IllegalAccessException;

    void checkString(ExcelColumn excelColumn, Object bean, Field field, String value, int rowIndex, List<ApiError> errors) throws IllegalAccessException;

}
