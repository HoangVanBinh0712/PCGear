package pc.gear.response.product;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import pc.gear.dto.interfaces.ISearchResponse;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ProductSearchResponse implements ISearchResponse<ProductSearchResponse.ProductSearchItem> {

    Integer totalRow;

    List<ProductSearchItem> contents;

    @Getter
    @Setter
//    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class ProductSearchItem extends GetProductByCodeResponse {

    }
}
