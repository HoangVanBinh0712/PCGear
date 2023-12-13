package pc.gear.security.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import pc.gear.dto.User;
import pc.gear.service.JwtService;
import pc.gear.service.impl.AdminDetailsServiceImpl;
import pc.gear.service.impl.CustomerDetailsServiceImpl;
import pc.gear.util.Constants;
import pc.gear.util.MessageConstants;
import pc.gear.util.type.DepartmentType;
import pc.gear.util.type.TokenValidateType;

import java.io.IOException;

@Component
public class TokenFilter extends OncePerRequestFilter {

    @Value(value = "${security.skipUrl}")
    private String skipUrl;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private AdminDetailsServiceImpl adminDetailsService;

    @Autowired
    private CustomerDetailsServiceImpl customerDetailsService;
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
        String token = jwtService.getTokenFromRequest(request);
        if (isValid(token, request) == null) {
            throw new BadCredentialsException(messageSource.getMessage(MessageConstants.INVALID_CREDENTIALS, null, LocaleContextHolder.getLocale()));
        }
        doFilter(request, response, filterChain);
    }

    private TokenValidateType isValid(String token, HttpServletRequest request) {
        if (StringUtils.isNotBlank(token)) {
            // if invalid token throw error
            User user = jwtService.validateAccessToken(token);
            UserDetails userDetails;
            // Set user to context
            if (StringUtils.isBlank(user.getDepartmentCd()) || StringUtils.equals(user.getDepartmentCd(), DepartmentType.CUSTOMER.getKey())) {
                userDetails = customerDetailsService.loadUserByUsername(user.getUserName());
            } else {
                userDetails = adminDetailsService.loadUserByUsername(user.getUserName());
            }
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return TokenValidateType.ORIGINAL;
        }
        return null;
    }

}
