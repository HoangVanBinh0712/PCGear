package pc.gear.repository.custom;

import pc.gear.request.product.ProductSearchRequest;
import pc.gear.response.product.GetProductByCodeResponse;
import pc.gear.response.product.ProductSearchResponse;

public interface ProductCustomRepository {
    GetProductByCodeResponse getProductByCode(String productCode);

    ProductSearchResponse search(ProductSearchRequest request);
}
