package pc.gear.response.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import pc.gear.response.product.GetProductByCodeResponse;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class GetCustomerOrderResponse {

    List<CustomerOrder> orders;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class CustomerOrder {

        @Column(name = "order_id")
        Long orderId;

        @Column(name = "order_date")
        Timestamp orderTimestamp;

        @Column(name = "note")
        String note;

        @Column(name = "total_amount")
        BigDecimal totalAmount;


        @Column(name = "created_datetime")
        Timestamp createdTimestamp;

        @Column(name = "is_paid")
        Boolean isPaid;

        @Column(name = "payment_datetime")
        Timestamp paidTimestamp;

        List<CustomerOrderItem> orderItems;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class CustomerOrderItem extends GetProductByCodeResponse {
        @Column(name = "order_item_id")
        Long orderItemId;

        @JsonIgnore
        @Column(name = "order_id")
        Long orderId;

        @Column(name = "order_quantity")
        Integer orderQuantity;

        @Column(name = "order_price")
        BigDecimal orderPrice;

        @Column(name = "order_discount")
        BigDecimal orderDiscount;
    }
}
