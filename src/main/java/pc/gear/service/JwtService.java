package pc.gear.service;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import pc.gear.entity.Customer;

public interface JwtService {
    String generateCustomerAccessToken(Customer customer);

    String getTokenFromRequest(HttpServletRequest request);

    Claims validateToken(String token);
}
