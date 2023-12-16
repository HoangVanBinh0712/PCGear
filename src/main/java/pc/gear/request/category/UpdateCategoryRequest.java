package pc.gear.request.category;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateCategoryRequest extends AddCategoryRequest {
    @NotNull
    private Long categoryId;

    @NotNull
    private Boolean deleteFlag;

}
