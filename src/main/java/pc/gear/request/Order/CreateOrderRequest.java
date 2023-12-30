package pc.gear.request.Order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {
    @Valid
    List<CreateOrder> carts;

    @Size(max = 500)
    String note;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class CreateOrder {

        Long cartId;

        @NotNull
        @Min(1)
        Integer quantity;

        @NotNull
        Long productId;

        LocalDateTime productUpdatedDatetime;

    }
}
