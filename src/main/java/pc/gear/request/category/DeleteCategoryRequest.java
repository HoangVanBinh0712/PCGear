package pc.gear.request.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import pc.gear.annotaion.interfaces.ListItem;
import pc.gear.annotaion.interfaces.ValidateMultipartFile;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DeleteCategoryRequest {

    @ListItem(isCheckNumber = true, integerPart = 10, fractionPart = 0, listSize = 2)
    private List<BigDecimal> categoryIds;

    @ListItem(isCheckString = true, minLength = 10, maxLength = 10, listSize = 2)
    private List<String> strCategoryIds;

    @ValidateMultipartFile(extensions = { "xlsx", "xlsm", "xls" })
    private MultipartFile multipartFile;
}
