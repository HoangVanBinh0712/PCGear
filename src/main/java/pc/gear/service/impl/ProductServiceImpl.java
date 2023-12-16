package pc.gear.service.impl;

import io.micrometer.common.util.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pc.gear.repository.CategoryRepository;
import pc.gear.repository.ProductRepository;
import pc.gear.response.product.GetProductByCodeResponse;
import pc.gear.service.BaseService;
import pc.gear.service.ProductService;
import pc.gear.util.MessageConstants;
import pc.gear.validator.ProductValidator;

@Service
public class ProductServiceImpl extends BaseService implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ProductValidator productValidator;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public GetProductByCodeResponse getByProductCode(String productCode) {
        // throw error if blank code
        if (StringUtils.isBlank(productCode)) {
            this.throwError(MessageConstants.MESSAGE_NOT_BLANK, this.getMessage(MessageConstants.PRODUCT_CODE));
        }
        GetProductByCodeResponse response = productRepository.getProductByCode(productCode);
        if (response == null) {
            // throw error 404
            this.throwErrorNotFound(MessageConstants.DATA_NOT_FOUND, this.getMessage(MessageConstants.PRODUCT));
        }
        return response;
    }
}
