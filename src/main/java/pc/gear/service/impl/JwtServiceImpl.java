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
import pc.gear.dto.User;
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

    @Value("${jwt.refresh.expirationMs}")
    private long refreshExpirationMs;

    @Value("${jwt.refresh.secret}")
    private String refreshSecret;

    @Value("${server.name}")
    private String serverName;

    @Override
    public String generateCustomerAccessToken(User user) {
        return generateToken(user, accessSecret, accessExpirationMs);
    }

    @Override
    public String generateCustomerRefreshToken(User user) {
        return generateToken(user, refreshSecret, refreshExpirationMs);
    }

    /**
     * 
     * Generate token with secret key
     * 
     * @param user Customer, secret String
     * @return token
     * @author BinhSenpai
     */
    private String generateToken(User user, String secret, long expirationMs) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(Constants.CLAIMS_USER_NAME, user.getUserName());
        claims.put(Constants.CLAIMS_NAME, user.getName());
        claims.put(Constants.CLAIMS_EMAIL, user.getEmail());
        claims.put(Constants.CLAIMS_USER_ID, user.getUserID());
        claims.put(Constants.CLAIMS_DEPARTMENT, user.getDepartmentCd());
        Date today = new Date();
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .issuer(serverName)
                .claims(claims)
                .issuedAt(today)
                .expiration(new Date(today.getTime() + expirationMs))
                .signWith(key)
                .compact();
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
    public User validateAccessToken(String token) {
        return validateToken(token, accessSecret);
    }

    @Override
    public User validateRefreshToken(String token) {
        return validateToken(token, refreshSecret);
    }

    /**
     *
     * validate token with secret key
     *
     * @param token String, accessSecret String
     * @return User (Body of token)
     * @author BinhSenpai
     */
    private User validateToken(String token, String accessSecret) {
        SecretKey key = Keys.hmacShaKeyFor(accessSecret.getBytes(StandardCharsets.UTF_8));
        JwtParser jwtParser = Jwts.parser()
                .verifyWith(key)
                .build();
        try {
            Claims claims = (Claims) jwtParser.parse(token).getPayload();
            return new User(claims);
        } catch (Exception e) {
            throw new PcGearException(e.getMessage());
        }
    }

}
