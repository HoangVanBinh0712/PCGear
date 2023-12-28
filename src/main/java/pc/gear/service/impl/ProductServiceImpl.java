package pc.gear.service.impl;

import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pc.gear.repository.ProductRepository;
import pc.gear.request.product.ProductSearchRequest;
import pc.gear.response.product.GetProductByCodeResponse;
import pc.gear.response.product.ProductSearchResponse;
import pc.gear.service.BaseService;
import pc.gear.service.ProductService;
import pc.gear.util.MessageConstants;

@Service
public class ProductServiceImpl extends BaseService implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public GetProductByCodeResponse getByProductCode(String productCode) {
        // throw error if blank code
        if (StringUtils.isBlank(productCode)) {
            this.throwError(MessageConstants.NOT_BLANK, this.getMessage(MessageConstants.PRODUCT_CODE));
        }
        GetProductByCodeResponse response = productRepository.getProductByCode(productCode);
        if (response == null) {
            // throw error 404
            this.throwErrorNotFound(MessageConstants.DATA_NOT_FOUND, this.getMessage(MessageConstants.PRODUCT));
        }
        return response;
    }

    @Override
    public ProductSearchResponse search(ProductSearchRequest request) {
        this.validateSearchRequest(request, "productApi.ProductSearchRequest.sortFields.value",
                "productApi.ProductSearchRequest.sortFields.message");
        return productRepository.search(request);
    }
}
