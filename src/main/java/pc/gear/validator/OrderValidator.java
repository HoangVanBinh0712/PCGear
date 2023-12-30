package pc.gear.validator;

import pc.gear.entity.Product;
import pc.gear.request.Order.CreateOrderRequest;

import java.time.LocalDateTime;
import java.util.Map;

public interface OrderValidator {
    Map<Long, Product> validateCreate(CreateOrderRequest request, LocalDateTime currentDate);
}
