package pc.gear.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestRequest {
    @NotBlank
    @Size(max = 20)
    private String name;

    @NotNull
    @Digits(integer = 3, fraction = 2)
    private Double mark;
}
