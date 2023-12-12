package pc.gear.service;

import pc.gear.entity.Customer;

public interface JwtService {
    String generateCustomerAccessToken(Customer customer);
}
