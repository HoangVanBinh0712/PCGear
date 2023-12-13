package pc.gear.service;

import jakarta.servlet.http.HttpServletRequest;
import pc.gear.dto.User;

public interface JwtService {
    /**
     *
     * generate access token
     *
     * @param user User
     * @return Token
     * @author BinhSenpai
     */
    String generateCustomerAccessToken(User user);

    /**
     *
     * generate access token
     *
     * @param user User
     * @return Token
     * @author BinhSenpai
     */
    String generateCustomerRefreshToken(User user);

    /**
     *
     * Get token from request. (Authoriaztion)
     *
     * @param request HttpServletRequest
     * @return Token in request.
     * @author BinhSenpai
     */
    String getTokenFromRequest(HttpServletRequest request);

    /**
     *
     * validate token with secret key
     *
     * @param token String, accessSecret String
     * @return User (Body of token)
     * @author BinhSenpai
     */
    User validateAccessToken(String token);

    /**
     *
     * validate token with secret key
     *
     * @param token String, accessSecret String
     * @return User (Body of token)
     * @author BinhSenpai
     */
    User validateRefreshToken(String token);
}
