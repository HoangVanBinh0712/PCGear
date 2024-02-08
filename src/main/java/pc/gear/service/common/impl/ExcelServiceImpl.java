package pc.gear.service.common.impl;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import pc.gear.annotaion.interfaces.ExcelColumn;
import pc.gear.annotaion.interfaces.IntegerNumber;
import pc.gear.annotaion.interfaces.TextArea;
import pc.gear.dto.excel.ImportProductDto;
import pc.gear.entity.Category;
import pc.gear.entity.Product;
import pc.gear.service.BaseService;
import pc.gear.service.common.ExcelService;
import pc.gear.util.Constants;
import pc.gear.util.MessageConstants;
import pc.gear.util.lang.DateUtil;
import pc.gear.util.lang.ExcelUtil;
import pc.gear.util.lang.NumberUtil;
import pc.gear.util.lang.StringUtil;
import pc.gear.util.response.ApiError;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class ExcelServiceImpl implements ExcelService {

    @Autowired
    private BaseService baseService;


    @Override
    public void readObject(Map<String, Sheet> sheetMap, Object bean, int rowIndex, List<ApiError> errors) throws IllegalAccessException {
        for (Field field : bean.getClass().getDeclaredFields()) {
            readField(sheetMap, bean, field, rowIndex, errors);
        }
    }

    @Override
    public void readObjectImportProduct(Map<String, Sheet> sheetMap, ImportProductDto bean, int rowIndex, List<ApiError> errors, List<Category> categories) throws IllegalAccessException {
        for (Field field : bean.getClass().getDeclaredFields()) {
            readField(sheetMap, bean, field, rowIndex, errors);
        }
        // validate a category
        Category category = categories.stream()
                .filter(x -> StringUtil.equal(x.getCategoryCd(), bean.getCategory())).findFirst().orElse(null);
        if (category == null) {
            errors.add(baseService.getApiErrorForExcel(Constants.PRODUCT_SHEET, rowIndex,
                    "", MessageConstants.DATA_NOT_FOUND, baseService.getMessage(MessageConstants.CATEGORY)));
        } else {
            bean.setCategoryEntity(category);
        }

    }

    @Override
    public void readField(Map<String, Sheet> sheetMap, Object bean, Field field, int rowIndex, List<ApiError> errors) throws IllegalAccessException {
        field.setAccessible(true);
        if (field.isAnnotationPresent(ExcelColumn.class)) {
            ExcelColumn excelColumn = field.getAnnotation(ExcelColumn.class);
            int colIndex = ExcelUtil.getColIndex(excelColumn.columnName());
            // If workbook has the sheet name
            if (sheetMap.containsKey(excelColumn.sheetName())) {
                String value = ExcelUtil.getDataFromCell(sheetMap.get(excelColumn.sheetName()), rowIndex, colIndex);
                if (String.class.equals(field.getType())) {
                    checkString(excelColumn, bean, field, value, rowIndex, errors);
                } else if (BigDecimal.class.equals(field.getType())) {
                    checkNumber(excelColumn, bean, field, value, rowIndex, errors);
                }
            }
        }
    }

    @Override
    public void checkNumber(ExcelColumn excelColumn, Object bean, Field field, String value, int rowIndex, List<ApiError> errors) throws IllegalAccessException {
        if (StringUtils.isNotBlank(value)) {
            if (!NumberUtils.isCreatable(value)) {
                // Add error
                errors.add(baseService.getApiErrorForExcel(excelColumn.sheetName(), rowIndex,
                        "", "system.number-invalid.error.message", excelColumn.fieldName()));
                return;
            }
            if (field.isAnnotationPresent(Digits.class)) {
                Digits digitValidate = field.getAnnotation(Digits.class);
                if (!NumberUtil.validateFormatNumber(value, digitValidate.integer(), digitValidate.fraction())) {
                    // Add error
                    errors.add(baseService.getApiErrorForExcel(excelColumn.sheetName(), rowIndex,
                            "", "system.digits.error.message", excelColumn.fieldName(),
                            NumberUtil.generateMessageDigit(digitValidate.integer(), digitValidate.fraction())));
                } else {
                    // if no error
                    field.set(bean, NumberUtil.convertToType(value, field.getType()));
                }

            }
            if (field.isAnnotationPresent(IntegerNumber.class)) {
                IntegerNumber integerNumber = field.getAnnotation(IntegerNumber.class);
                if (!NumberUtil.validateIntegerNumber(value, integerNumber.max())) {
                    errors.add(baseService.getApiErrorForExcel(excelColumn.sheetName(), rowIndex,
                            "", "system.integer-number.error.message", excelColumn.fieldName(), integerNumber.max()));
                } else {
                    field.set(bean, NumberUtil.convertToType(value, field.getType()));
                }
            }
        }
    }

    @Override
    public void checkString(ExcelColumn excelColumn, Object bean, Field field, String value, int rowIndex, List<ApiError> errors) throws IllegalAccessException {
        boolean hasError = false;
        if (field.isAnnotationPresent(NotBlank.class) && StringUtils.isBlank(value)) {
            errors.add(baseService.getApiErrorForExcel(excelColumn.sheetName(), rowIndex,
                    "", "jakarta.validation.constraints.NotBlank.message", excelColumn.fieldName()));
        }
        if (field.isAnnotationPresent(Size.class)) {
            Size size = field.getAnnotation(Size.class);
            if (!StringUtil.validateLengthForPoint(value, size.min(), size.max())) {
                // Add error
                hasError = true;
                errors.add(baseService.getApiErrorForExcel(excelColumn.sheetName(), rowIndex,
                        "", "system.size.error.message", excelColumn.fieldName(), size.min(), size.max()));
            }

        }
        if (field.isAnnotationPresent(TextArea.class)) {
            TextArea textArea = field.getAnnotation(TextArea.class);
            if (!StringUtil.validateTextArea(value, textArea.min(), textArea.max())) {
                // Add error
                hasError = true;
                errors.add(baseService.getApiErrorForExcel(excelColumn.sheetName(), rowIndex,
                        "", "system.size.error.message", excelColumn.fieldName(), textArea.min(), textArea.max()));
            }

        }
        if (field.isAnnotationPresent(DateTimeFormat.class)) {
            DateTimeFormat dateTimeFormat = field.getAnnotation(DateTimeFormat.class);
            if (!DateUtil.isValidLocalDatetime(value, dateTimeFormat.pattern())) {
                // Add error
                hasError = true;
                errors.add(baseService.getApiErrorForExcel(excelColumn.sheetName(), rowIndex,
                        "", "system.date.error.message", excelColumn.fieldName(), dateTimeFormat.pattern()));
            }
        }
        if (!hasError) {
            // Set value
            field.set(bean, value);
        }
    }

}
