package pc.gear.service.impl;

import jakarta.transaction.Transactional;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pc.gear.entity.CustomerOrder;
import pc.gear.entity.OrderItem;
import pc.gear.entity.Product;
import pc.gear.repository.CustomerOderRepository;
import pc.gear.repository.CustomerRepository;
import pc.gear.repository.OrderItemRepository;
import pc.gear.request.Order.CreateOrderRequest;
import pc.gear.request.cart.DeleteCartRequest;
import pc.gear.response.order.GetCustomerOrderResponse;
import pc.gear.service.BaseService;
import pc.gear.service.OrderService;
import pc.gear.util.JwtUtil;
import pc.gear.validator.OrderValidator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class OrderServiceImpl extends BaseService implements OrderService {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CustomerOderRepository customerOderRepository;

    @Autowired
    private OrderValidator orderValidator;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CartService cartService;

    @Override
    @Transactional
    public void create(CreateOrderRequest request) {
        LocalDateTime currentDate = LocalDateTime.now();

        Map<Long, Product> productMap = orderValidator.validateCreate(request, currentDate);

        List<OrderItem> orderItems = new ArrayList<>();
        CustomerOrder order = new CustomerOrder();
        for (CreateOrderRequest.CreateOrder cart : request.getCarts()) {
            Product product = productMap.get(cart.getProductId());
            getOrderItem(cart, order, product, currentDate, orderItems);
        }

        setDataCustomerOder(request, order, currentDate, orderItems);
        customerOderRepository.save(order);
        // Delete cart
        List<Long> cartIds = request.getCarts().stream().map(CreateOrderRequest.CreateOrder::getCartId).filter(Objects::nonNull).toList();
        cartService.deleteCart(new DeleteCartRequest(cartIds));
    }

    @Override
    public GetCustomerOrderResponse get() {
        return customerRepository.getCustomerOrders();
    }


    private void setDataCustomerOder(CreateOrderRequest request, CustomerOrder order, LocalDateTime currentDate, List<OrderItem> orderItems) {
        order.setCustomer(customerRepository.getReferenceById(JwtUtil.getCurrentUserId()));
        order.setOrderDate(currentDate);
        order.setNote(request.getNote());
        order.setOrderItems(orderItems);
        // Calculate total amount
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (OrderItem item : orderItems) {
            BigDecimal coefficient = BigDecimal.ONE;
            BigDecimal quantity = BigDecimal.valueOf(item.getQuantity());
            if (item.getDiscount() != null) {
                coefficient = coefficient.subtract(item.getDiscount().divide(new BigDecimal(100), 4, RoundingMode.HALF_UP));
            }
            totalAmount = totalAmount.add(
                    item.getPrice()
                            .multiply(quantity)
                            .multiply(coefficient));
        }
        order.setTotalAmount(totalAmount);
    }

    private void getOrderItem(CreateOrderRequest.CreateOrder cart, CustomerOrder order, Product product, LocalDateTime currentDate, List<OrderItem> orderItems) {
        // The data is correct
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setPrice(product.getPrice());
        orderItem.setQuantity(cart.getQuantity());
        // Set discount
        // Check that current date is valid for discount
        LocalDateTime discountFrom = ObjectUtils.defaultIfNull(product.getDiscountFrom(), LocalDateTime.MIN);
        LocalDateTime discountTo = ObjectUtils.defaultIfNull(product.getDiscountTo(), LocalDateTime.MAX);
        if (product.getDiscount() != null && currentDate.isAfter(discountFrom) && currentDate.isBefore(discountTo)) {
            orderItem.setDiscount(product.getDiscount());
        }
        orderItems.add(orderItem);
    }
}
