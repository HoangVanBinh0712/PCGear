package pc.gear.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pc.gear.config.exception.PcGearException;
import pc.gear.entity.Customer;
import pc.gear.service.JwtService;
import pc.gear.util.Constants;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
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
        SecretKey key = Keys.hmacShaKeyFor(accessSecret.getBytes(StandardCharsets.UTF_8));
        String jws = Jwts.builder()
                .issuer(serverName)
                .claims(claims)
                .issuedAt(today)
                .expiration(new Date(today.getTime() + accessExpirationMs))
                .signWith(key)
                .compact();
        return jws;
    }

    @Override
    public String getTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader(Constants.AUTHENTICATION);
        if (StringUtils.isNotBlank(token)) {
            if (token.startsWith(Constants.BEARER)) {
                return token.substring(Constants.BEARER.length());
            }
            return token;
        }
        return Constants.EMPTY;
    }

    @Override
    public Claims validateToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(accessSecret.getBytes(StandardCharsets.UTF_8));
        JwtParser jwtParser = Jwts.parser()
                .verifyWith(key)
                .build();
        try {
            Claims claims = (Claims) jwtParser.parse(token).getPayload();
            claims.get("userId", Long.class);
            return claims;
        } catch (Exception e) {
            throw new PcGearException(e.getMessage());
        }
    }

}
