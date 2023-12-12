package pc.gear.request.auth;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NotBlank
    @Size(max = 255)
    private String name;

    @NotBlank
    @Size(max = 50, min = 5)
    private String userName;

    @Size(max = 255)
    private String email;

    @NotBlank
    @Size(max = 50)
    private String password;

    @Size(max = 1000)
    private String address;

    @Size(max = 20)
    private String phoneNumber;
}
