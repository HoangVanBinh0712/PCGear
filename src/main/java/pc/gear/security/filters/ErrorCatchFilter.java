package pc.gear.security.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import pc.gear.config.exception.GlobalExceptionHandler;
import pc.gear.util.response.ApiError;
import pc.gear.util.response.ApiResponse;

import java.io.IOException;
import java.util.Collections;

@Component
public class ErrorCatchFilter extends OncePerRequestFilter {
    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            doFilter(request, response, filterChain);
        } catch (Exception e) {
            // if there is any exception during the filer Call Controller advice
            // to know what type of exception is throw then return the correct status and message
            ApiError error = globalExceptionHandler.handleError(e);
            // Set the response type to JSON
            response.setContentType("application/json");
            ApiResponse<Object> apiResponse = ApiResponse.builder()
                    .errors(Collections.singletonList(error))
                    .build();
            // Convert the Map to JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(apiResponse);

            // Write the JSON response to the HttpServletResponse
            response.getWriter().write(jsonResponse);

            // Set the HTTP status code to 400
            response.setStatus(Integer.parseInt(error.getCode()));
        }
    }

}
