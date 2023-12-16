package pc.gear.service;

import pc.gear.request.auth.LoginRequest;
import pc.gear.request.auth.RegisterRequest;
import pc.gear.response.auth.LoginResponse;

public interface UserService {
    LoginResponse customerLogin(LoginRequest request);

    LoginResponse adminLoginLogin(LoginRequest request);

    void customerRegister(RegisterRequest request);
}
