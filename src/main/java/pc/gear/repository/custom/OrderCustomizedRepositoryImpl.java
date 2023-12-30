package pc.gear.repository.custom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import pc.gear.config.ColumnMapper;
import pc.gear.response.order.GetCustomerOrderResponse;
import pc.gear.service.BaseService;
import pc.gear.util.JwtUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class OrderCustomizedRepositoryImpl extends BaseService implements OrderCustomizedRepository {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public GetCustomerOrderResponse getCustomerOrders() {
        Long userId = JwtUtil.getCurrentUserId();
        List<GetCustomerOrderResponse.CustomerOrder> orders = getGetCustomerOrders(userId);
        List<Long> orderIds = orders.stream().map(GetCustomerOrderResponse.CustomerOrder::getOrderId).toList();
        // Get order item by orderIds
        if (!CollectionUtils.isEmpty(orderIds)) {
            List<GetCustomerOrderResponse.CustomerOrderItem> orderItems = getGetCustomerOrderItems(orderIds);
            orders.forEach(order -> {
                orderItems.forEach(item -> {
                    if (Objects.equals(order.getOrderId(), item.getOrderId())) {
                        if (order.getOrderItems() == null) {
                            order.setOrderItems(new ArrayList<>());
                        }
                        order.getOrderItems().add(item);
                    }
                });
            });
        }
        return GetCustomerOrderResponse.builder().orders(orders).build();
    }

    private List<GetCustomerOrderResponse.CustomerOrder> getGetCustomerOrders(Long userId) {
        String sql = """
                select c.order_id,
                       c.order_date,
                       c.note,
                       c.total_amount,
                       c.created_datetime,
                       p.created_datetime             as payment_datetime,
                       if(p.payment_id is null, 0, 1) as is_paid
                from customer_order c
                         left join payment p on c.order_id = p.order_id
                where c.customer_id = :customerId
                                """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("customerId", userId);
        return jdbcTemplate.query(sql, params, ColumnMapper.newInstance(GetCustomerOrderResponse.CustomerOrder.class));
    }

    private List<GetCustomerOrderResponse.CustomerOrderItem> getGetCustomerOrderItems(List<Long> orderIds) {
        String sql = """
                select o.order_item_id,
                       o.quantity as order_quantity,
                       o.price    as order_price,
                       o.discount as order_discount,
                       o.order_id,
                       p.product_code,
                       p.title,
                       p.description,
                       p.price,
                       p.stock,
                       p.discount,
                       p.discount_from,
                       p.discount_to,
                       p.image,
                       p.delete_fg,
                       p.updated_datetime,
                       c.category_cd,
                       c.name     as category_name
                from order_item o
                         left join product p on o.product_id = p.product_id
                         left join category c
                                   on p.category_id = c.category_id
                where order_id in (:orderIds)
                                                """;
        MapSqlParameterSource params = new MapSqlParameterSource();

        params.addValue("orderIds", orderIds);
        return jdbcTemplate.query(sql, params, ColumnMapper.newInstance(GetCustomerOrderResponse.CustomerOrderItem.class));
    }
}
