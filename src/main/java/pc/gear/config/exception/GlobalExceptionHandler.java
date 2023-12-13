package pc.gear.config.exception;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Size;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.HandlerMethod;
import pc.gear.util.Constants;
import pc.gear.util.NumberUtil;
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

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ApiResponse<?>> handleException(Exception exception) {
//        return new ResponseEntity<>(new ApiResponse<>(Collections.singletonList(
//                new ApiError(exception.getMessage(), Constants.INTERNAL_SERVER_ERROR_CODE)
//        )), HttpStatus.INTERNAL_SERVER_ERROR);
//    }

    @ExceptionHandler(PcGearException.class)
    public ResponseEntity<ApiResponse<?>> handlePcGearException(PcGearException exception) {
        return new ResponseEntity<>(new ApiResponse<>(Collections.singletonList(
                new ApiError(exception.getMessage(), Constants.BAD_REQEST_CODE)
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
        return new ResponseEntity<>(new ApiResponse<>(List.of(new ApiError(messageSource.getMessage("TypeMissMatch.message", null, LocaleContextHolder.getLocale()), Constants.BAD_REQEST_CODE))), HttpStatus.BAD_REQUEST);
    }


    private ApiError handleErrorArgument(HandlerMethod handlerMethod, FieldError fieldError) {
        ApiError error = new ApiError();
        String fieldNameError = handlerMethod.getBeanType().getSimpleName()
                + Constants.DOT + fieldError.getObjectName()
                + Constants.DOT + fieldError.getField();
        String fieldNameMessage = messageSource.getMessage(fieldNameError, null, LocaleContextHolder.getLocale());
        String message = fieldError.getDefaultMessage();
        Object[] arguments = fieldError.getArguments();
        if (Digits.class.getSimpleName().equals(fieldError.getCode()) && arguments != null && arguments.length >= 2) {
            setMessage(error, message, new Object[] { fieldNameMessage,
                    NumberUtil.getNumberFormat((int) arguments[2], (int) arguments[1]) });
        } else if (Size.class.getSimpleName().equals(fieldError.getCode()) && arguments != null && arguments.length >= 2) {
            setMessage(error, message, new Object[] { fieldNameMessage, arguments[2], arguments[1] });
        } else {
            setMessage(error, message, new Object[] { fieldNameMessage });
        }
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
