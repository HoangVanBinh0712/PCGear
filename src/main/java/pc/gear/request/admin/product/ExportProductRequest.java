package pc.gear.request.admin.product;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;
import pc.gear.annotaion.interfaces.ExcelColumn;
import pc.gear.annotaion.interfaces.IntegerNumber;
import pc.gear.annotaion.interfaces.TextArea;
import pc.gear.annotaion.interfaces.ValidateMultipartFile;
import pc.gear.entity.Category;
import pc.gear.util.Constants;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExportProductRequest {

    @Valid
    private List<ExportProduct> products;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ExportProduct {

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
        @Size(max = 16)
        private String discountFrom;

        @ExcelColumn(sheetName = Constants.PRODUCT_SHEET, columnName = "K", fieldName = "Discount to")
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
        @Size(max = 16)
        private String discountTo;

        @ExcelColumn(sheetName = Constants.PRODUCT_SHEET, columnName = "L", fieldName = "Stock")
        @IntegerNumber(max = 8)
        private BigDecimal stock;

        // set product No for save product (when import will update)
        @ExcelColumn(sheetName = Constants.PRODUCT_SHEET, columnName = "N", fieldName = "Product No")
        private Long productNo;
    }
}
