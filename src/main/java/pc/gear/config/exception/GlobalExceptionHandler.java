package pc.gear.config.exception;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.server.MethodNotAllowedException;
import pc.gear.util.Constants;
import pc.gear.util.MessageConstants;
import pc.gear.util.StringUtil;
import pc.gear.util.response.ApiError;
import pc.gear.util.response.ApiResponse;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleException(Exception exception) {
        exception.printStackTrace();
        return new ResponseEntity<>(new ApiResponse<>(Collections.singletonList(
                new ApiError(exception.getMessage(), Constants.INTERNAL_SERVER_ERROR_CODE)
        )), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ApiError handleError(Exception e) {
        log.error(e);
        e.printStackTrace();
        Integer status = null;
        if (e instanceof AccessDeniedException) {
            // status 403
            status = HttpStatus.FORBIDDEN.value();
        } else if (e instanceof PcGearException || e instanceof UsernameNotFoundException) {
            // Status 400
            status = HttpStatus.BAD_REQUEST.value();
        } else if (e instanceof BadCredentialsException) {
            // status 401
            status = HttpStatus.UNAUTHORIZED.value();
        } else if (e instanceof MethodNotAllowedException) {
            // status 405
            status = HttpStatus.METHOD_NOT_ALLOWED.value();
        } else if (e instanceof PcGearNotFoundException) {
            status = HttpStatus.NOT_FOUND.value();
        } else {
            // default 500
            status = HttpStatus.INTERNAL_SERVER_ERROR.value();
        }
        return ApiError.builder().code(String.valueOf(status)).message(e.getMessage()).build();
    }

    @ExceptionHandler({ PcGearException.class, PcGearNotFoundException.class })
    public ResponseEntity<ApiResponse<?>> handlePcGearException(Exception exception) {
        return new ResponseEntity<>(new ApiResponse<>(Collections.singletonList(
                new ApiError(exception.getMessage(), Constants.BAD_REQUEST_CODE)
        )), HttpStatus.BAD_REQUEST);
    }



    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HandlerMethod handlerMethod) {
        List<ApiError> errors = new ArrayList<>();
        ex.getFieldErrors().forEach(fieldError -> {
            ApiError error = handleErrorArgument(handlerMethod, fieldError);
            errors.add(error);
        });
        return new ResponseEntity<>(new ApiResponse<>(errors), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.info(ex.getMessage());
        return new ResponseEntity<>(new ApiResponse<>(List.of(new ApiError(messageSource.getMessage(MessageConstants.TYPE_MISS_MATCH, null, LocaleContextHolder.getLocale()), Constants.BAD_REQUEST_CODE))), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ViolationListException.class)
    public ResponseEntity<Object> handleViolationListException(ViolationListException ex) {
        return new ResponseEntity<>(new ApiResponse<>(ex.getViolations()), HttpStatus.BAD_REQUEST);
    }


    private ApiError handleErrorArgument(HandlerMethod handlerMethod, FieldError fieldError) {
        ApiError error = new ApiError();
        String fieldNameError = handlerMethod.getBeanType().getSimpleName()
                + Constants.DOT + fieldError.getObjectName()
                + Constants.DOT + StringUtil.removeIndexFromString(fieldError.getField());
        String fieldNameMessage = messageSource.getMessage(fieldNameError, null, LocaleContextHolder.getLocale());
        String message = fieldError.getDefaultMessage();
        setMessage(error, message, new Object[] { fieldNameMessage });
        error.setCode(fieldError.getField());
        return error;
    }

    private void setMessage(ApiError error, String message, Object[] param) {
        if (StringUtils.isNotBlank(message)) {
            try {
                MessageFormat messageFormat = new MessageFormat(message);
                error.setMessage(messageFormat.format(param));
            } catch (Exception e) {
                log.error(e);
            }
        }
    }
}
