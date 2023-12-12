package pc.gear.service.impl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pc.gear.entity.Customer;
import pc.gear.service.JwtService;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.access.expirationMs}")
    private long accessExpirationMs;

    @Value("${jwt.access.secret}")
    private String accessSecret;

    @Value("${server.name}")
    private String serverName;

    @Override
    public String generateCustomerAccessToken(Customer customer) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", customer.getUserName());
        claims.put("name", customer.getName());
        claims.put("email", customer.getEmail());
        claims.put("userId", customer.getCustomerId());
        Date today = new Date();
        String jws = Jwts.builder()
                .issuer(serverName)
                .claims(claims)
                // Fri Jun 24 2016 15:33:42 GMT-0400 (EDT)
                .issuedAt(today)
                // Sat Jun 24 2116 15:33:42 GMT-0400 (EDT)
                .expiration(new Date(today.getTime() + accessExpirationMs))
                .signWith(
                        SignatureAlgorithm.HS512,
                        accessSecret
                )
                .compact();
        return jws;
    }
}
