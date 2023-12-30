package pc.gear.service;

import pc.gear.request.Order.CreateOrderRequest;
import pc.gear.response.order.GetCustomerOrderResponse;

public interface OrderService {
    void create(CreateOrderRequest request);

    GetCustomerOrderResponse get();
}
