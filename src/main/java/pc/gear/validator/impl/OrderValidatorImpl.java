package pc.gear.validator.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import pc.gear.config.exception.ViolationListException;
import pc.gear.entity.Product;
import pc.gear.repository.ProductRepository;
import pc.gear.request.Order.CreateOrderRequest;
import pc.gear.service.BaseService;
import pc.gear.util.Constants;
import pc.gear.util.DateUtil;
import pc.gear.util.MessageConstants;
import pc.gear.util.response.ApiError;
import pc.gear.validator.OrderValidator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderValidatorImpl extends BaseService implements OrderValidator {

    @Autowired
    private ProductRepository productRepository;


    @Override
    public Map<Long, Product> validateCreate(CreateOrderRequest request, LocalDateTime currentDate) {
        List<ApiError> errors = new ArrayList<>();
        if (CollectionUtils.isEmpty(request.getCarts())) {
            errors.add(this.getBadRequestError(MessageConstants.ORDER_PRODUCT_LIST_EMPTY));
        }
        List<Long> productIds = request.getCarts().stream()
                .map(CreateOrderRequest.CreateOrder::getProductId).toList();
        // Get all product with the id in ids
        Map<Long, Product> productMap = productRepository
                .findByProductIdInAAndDeleteFlagIsFalse(productIds).stream()
                .collect(Collectors.toMap(Product::getProductId, x -> x));
        // If there are some product not found
        if (productIds.size() != productMap.size()) {
            errors.add(this.getBadRequestError(MessageConstants.DATA_NOT_FOUND, this.getMessage(MessageConstants.PRODUCT)));
        }
        for (CreateOrderRequest.CreateOrder cart : request.getCarts()) {
            if (productMap.containsKey(cart.getProductId())) {
                Product product = productMap.get(cart.getProductId());
                // If cart's updated datetime is not equal
                // If product's updated datetime is not equal
                if (DateUtil.compareLocalDate(product.getUpdatedDateTime(), cart.getProductUpdatedDatetime()) != Constants.NUMBER_ZERO) {
                    errors.add(this.getBadRequestError(MessageConstants.DIRTY_DATA));
                }
                // Check the product has enough stock
                if (cart.getQuantity().compareTo(product.getStock()) > Constants.NUMBER_ZERO) {
                    errors.add(this.getBadRequestError(MessageConstants.NOT_ENOUGH_STOCK, product.getTitle()));
                }
            }
        }
        if (!errors.isEmpty()) {
            throw new ViolationListException(errors);
        }
        return productMap;
    }

}
