package pc.gear.api;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pc.gear.request.auth.LoginRequest;
import pc.gear.request.auth.RegisterRequest;
import pc.gear.service.UserService;
import pc.gear.util.UriConstants;
import pc.gear.util.response.ApiResponse;

@RestController
public class AuthApi {

    @Autowired
    private UserService userService;

    @PostMapping(value = UriConstants.LOGIN)
    public ApiResponse<?> login(@RequestBody @Valid LoginRequest request) {
        return new ApiResponse<>(userService.customerLogin(request));
    }
    @PostMapping(value = UriConstants.ADMIN + UriConstants.LOGIN)
    public ApiResponse<?> adminLogin(@RequestBody @Valid LoginRequest request) {
        return new ApiResponse<>(userService.adminLoginLogin(request));
    }

    @PostMapping(value = UriConstants.REGISTER)
    public ApiResponse<?> register(@RequestBody @Valid RegisterRequest request) {
        userService.customerRegister(request);
        return new ApiResponse<>();
    }
}
