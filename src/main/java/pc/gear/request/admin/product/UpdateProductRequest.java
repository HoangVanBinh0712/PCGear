package pc.gear.request.admin.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProductRequest extends CreateProductRequest {

    private LocalDateTime updatedDateTime;

    @NotBlank
    private String productCode;

    @NotNull
    private Boolean deleteFg;
}
