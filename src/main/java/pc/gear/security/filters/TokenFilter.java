package pc.gear.security.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import pc.gear.util.Constants;

import java.io.IOException;

@Component
public class TokenFilter extends OncePerRequestFilter {

    @Value(value = "${security.skipUrl}")
    private String skipUrl;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        for (String url : skipUrl.split(Constants.COMMA)) {
            RequestMatcher requestMatcher = new AntPathRequestMatcher(url);
            if (requestMatcher.matches(request)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = getTokenFromRequest(request);
        if (isValid(token)) {
            doFilter(request, response, filterChain);
        } else {
            throw new BadCredentialsException("Invalid token");
        }
    }

    private boolean isValid(String token) {
        return false;
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        return request.getParameter("Authorization");
    }
}
