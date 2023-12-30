package pc.gear.service;

import pc.gear.request.Order.CreateOrderRequest;

public interface OrderService {
    void create(CreateOrderRequest request);
}
