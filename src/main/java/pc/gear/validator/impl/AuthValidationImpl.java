package pc.gear.validator.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pc.gear.entity.Customer;
import pc.gear.repository.CustomerRepository;
import pc.gear.request.auth.RegisterRequest;
import pc.gear.service.BaseService;
import pc.gear.util.MessageConstants;
import pc.gear.validator.AuthValidation;

@Service
public class AuthValidationImpl extends BaseService implements AuthValidation {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public void validateRegisterCustomer(RegisterRequest request) {
        // Check if username is already registered
        Customer customer = customerRepository.findTop1ByUserName(request.getUserName());
        if (customer != null) {
            this.throwError(MessageConstants.USERNAME_IS_REGISTERED_MESSAGE_CODE);
        }
    }
}
