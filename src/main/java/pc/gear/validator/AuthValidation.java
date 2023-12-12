package pc.gear.validator;

import pc.gear.request.auth.RegisterRequest;

public interface AuthValidation {
    void validateRegisterCustomer(RegisterRequest request);
}
