package pc.gear.security.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import pc.gear.config.exception.GlobalExceptionHandler;
import pc.gear.util.response.ApiError;
import pc.gear.util.response.ApiResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

@Component
@Log4j2
public class ErrorCatchAndLogFilter extends OncePerRequestFilter {
    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
        try {
            ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
            ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

            logRequest(requestWrapper);
            filterChain.doFilter(requestWrapper, responseWrapper);
            logRequest(requestWrapper);
            String requestBody = getRequestBody(requestWrapper);
            if (!requestBody.isEmpty()) {
                log.info("Request body: {}", requestBody.replaceAll("\\s", ""));
            }
            logResponse(responseWrapper);

            responseWrapper.copyBodyToResponse();

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


    private void logRequest(ContentCachingRequestWrapper request) throws IOException {
        log.info("Incoming request: {} {}", request.getMethod(), request.getRequestURI());
    }

    private void logResponse(ContentCachingResponseWrapper response) throws IOException {
        log.info("Outgoing response: status {}", response.getStatus());
        String responseBody = getResponseBody(response);
        if (!responseBody.isEmpty()) {
            log.info("Response body: {}", responseBody);
        }
    }

    private String getRequestBody(ContentCachingRequestWrapper request) {
        byte[] content = request.getContentAsByteArray();
        if (content.length > 0) {
            return new String(content, StandardCharsets.UTF_8);
        }
        return "";
    }

    private String getResponseBody(ContentCachingResponseWrapper response) {
        byte[] content = response.getContentAsByteArray();
        if (content.length > 0) {
            return new String(content, StandardCharsets.UTF_8);
        }
        return "";
    }

}
