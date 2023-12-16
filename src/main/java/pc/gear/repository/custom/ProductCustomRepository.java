package pc.gear.repository.custom;

import pc.gear.response.product.GetProductByCodeResponse;

public interface ProductCustomRepository {
    GetProductByCodeResponse getProductByCode(String productCode);
}
