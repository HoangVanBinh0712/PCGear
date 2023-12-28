package pc.gear.response.Cart;

import jakarta.persistence.Column;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import pc.gear.response.product.GetProductByCodeResponse;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class GetCartResponse {
    List<CartItem> carts;

    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class CartItem extends GetProductByCodeResponse {
        @Column(name = "cart_id")
        Long cartId;

        @Column(name = "quantity")
        Integer cartQuantity;

    }
}
