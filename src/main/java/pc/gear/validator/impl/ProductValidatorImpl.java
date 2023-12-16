package pc.gear.validator.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pc.gear.entity.Product;
import pc.gear.repository.CategoryRepository;
import pc.gear.repository.ProductRepository;
import pc.gear.request.admin.product.CreateProductRequest;
import pc.gear.request.admin.product.UpdateProductRequest;
import pc.gear.service.BaseService;
import pc.gear.util.DateUtil;
import pc.gear.util.MessageConstants;
import pc.gear.validator.ProductValidator;

import java.time.LocalDateTime;

@Service
public class ProductValidatorImpl extends BaseService implements ProductValidator {


    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public void validateProductCreate(CreateProductRequest request) {
        if(!categoryRepository.existsByCategoryIdAndDeleteFlagNot(request.getCategoryId(), Boolean.TRUE)){
            // The category is not exist or deleted
            this.throwError(MessageConstants.DATA_NOT_FOUND, this.getMessage(MessageConstants.CATEGORY));
        }
        if (request.getDiscountFrom() != null && request.getDiscountTo() != null) {
            if (request.getDiscountFrom().isAfter(request.getDiscountTo())) {
                this.throwError(MessageConstants.MUST_BE_GREATER_THAN,
                        this.getMessage("AdminProductApi.createProductRequest.discountTo"),
                        this.getMessage("AdminProductApi.createProductRequest.discountFrom"));
            }
        }
    }

    @Override
    public Product validateProductUpdate(UpdateProductRequest request) {
        validateProductCreate(request);
        Product p = productRepository.findByProductCode(request.getProductCode());
        if (p != null) {
            // Check last update datetime
            LocalDateTime dateTimeDb = DateUtil.defaultLocalDatetime(p.getUpdatedDateTime());
            LocalDateTime datetimeRequest = DateUtil.defaultLocalDatetime(request.getUpdatedDateTime());
            if (!dateTimeDb.isEqual(datetimeRequest)) {
                this.throwError(MessageConstants.DIRTY_DATA);
            }
            return p;
        }
        this.throwError(MessageConstants.DATA_NOT_FOUND, this.getMessage(MessageConstants.PRODUCT));
        return null;
    }
}
