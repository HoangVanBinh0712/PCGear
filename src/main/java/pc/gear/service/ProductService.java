package pc.gear.service;

import pc.gear.response.product.GetProductByCodeResponse;

public interface ProductService {
    GetProductByCodeResponse getByProductCode(String productCode);
}
