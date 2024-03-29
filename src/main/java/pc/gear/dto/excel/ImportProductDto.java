package pc.gear.dto.excel;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import pc.gear.annotaion.interfaces.ExcelColumn;
import pc.gear.annotaion.interfaces.IntegerNumber;
import pc.gear.annotaion.interfaces.TextArea;
import pc.gear.entity.Category;
import pc.gear.util.Constants;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImportProductDto {

    @ExcelColumn(sheetName = Constants.PRODUCT_SHEET, columnName = "E", fieldName = "Category")
    @Size(max = 50)
    @NotBlank
    private String category;

    @ExcelColumn(sheetName = Constants.PRODUCT_SHEET, columnName = "F", fieldName = "Title")
    @TextArea(max = 255)
    @NotBlank
    private String title;

    @ExcelColumn(sheetName = Constants.PRODUCT_SHEET, columnName = "G", fieldName = "Description")
    @TextArea(max = 10000)
    private String description;

    @ExcelColumn(sheetName = Constants.PRODUCT_SHEET, columnName = "H", fieldName = "Price")
    @Digits(integer = 36, fraction = 2)
    private BigDecimal price;

    @ExcelColumn(sheetName = Constants.PRODUCT_SHEET, columnName = "I", fieldName = "Discount(%)")
    @Digits(integer = 3, fraction = 0)
    private BigDecimal discount;

    @ExcelColumn(sheetName = Constants.PRODUCT_SHEET, columnName = "J", fieldName = "Discount from")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @Size(min = 16, max = 16)
    private String discountFrom;

    @ExcelColumn(sheetName = Constants.PRODUCT_SHEET, columnName = "K", fieldName = "Discount to")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @Size(min = 16, max = 16)
    private String discountTo;

    @ExcelColumn(sheetName = Constants.PRODUCT_SHEET, columnName = "L", fieldName = "Stock")
    @IntegerNumber(max = 8)
    private BigDecimal stock;

    private Category categoryEntity;
}
