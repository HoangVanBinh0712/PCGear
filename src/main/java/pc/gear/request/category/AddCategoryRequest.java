package pc.gear.request.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddCategoryRequest {
    @NotBlank
    @Size(max = 16)
    private String categoryCd;

    @NotBlank
    @Size(max = 50)
    private String name;

    @Size(max = 500)
    private String description;

}
