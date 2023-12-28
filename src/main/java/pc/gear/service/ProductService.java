package pc.gear.service;

import pc.gear.request.product.ProductSearchRequest;
import pc.gear.response.product.GetProductByCodeResponse;
import pc.gear.response.product.ProductSearchResponse;

public interface ProductService {
    GetProductByCodeResponse getByProductCode(String productCode);

    ProductSearchResponse search(ProductSearchRequest request);
}
