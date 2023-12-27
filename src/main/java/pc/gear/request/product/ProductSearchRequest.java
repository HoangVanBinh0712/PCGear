package pc.gear.request.product;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import pc.gear.dto.SearchRequest;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductSearchRequest extends SearchRequest {
    String textSearch;

    @Size(max = 16)
    String categoryCode;

    Boolean isDiscount;

    @Digits(integer = 18, fraction = 2)
    @Min(0)
    BigDecimal priceFrom;

    @Digits(integer = 18, fraction = 2)
    @Min(0)
    BigDecimal priceTo;

    public ProductSearchRequest(String textSearch, String categoryCode, Boolean isDiscount,
                                BigDecimal priceFrom, BigDecimal priceTo, Integer pageSize, Integer pageNumber,
                                String sortFields, String sortDirections) {
        super(pageSize, pageNumber, sortFields, sortDirections);
        this.textSearch = textSearch;
        this.categoryCode = categoryCode;
        this.isDiscount = isDiscount;
        this.priceFrom = priceFrom;
        this.priceTo = priceTo;
    }
}
