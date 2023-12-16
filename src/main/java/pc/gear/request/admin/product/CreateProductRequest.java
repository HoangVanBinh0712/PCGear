package pc.gear.request.admin.product;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductRequest {
    @NotBlank
    @Size(max = 255)
    private String title;

    @NotBlank
    @Size(max = 10000)
    private String description;

    @Digits(integer = 18, fraction = 2)
    @Min(0)
    private BigDecimal price;

    @Min(0)
    private Integer stock;

    @Digits(integer = 3, fraction = 2)
    @Min(0)
    @Max(100)
    private BigDecimal discount;

    private LocalDateTime discountFrom;

    private LocalDateTime discountTo;

    @NotBlank
    private String image;

    @NotNull
    private Long categoryId;
}
