package pc.gear.annotaion.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.multipart.MultipartFile;
import pc.gear.annotaion.interfaces.ValidateMultipartFile;
import pc.gear.service.BaseService;
import pc.gear.util.lang.MultipartUtil;
import pc.gear.util.lang.StringUtil;

import java.util.Arrays;

public class ValidMultipartFileValidator implements ConstraintValidator<ValidateMultipartFile, MultipartFile> {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private BaseService baseService;

    private String[] extensions;

    private int maxSize;

    private void setMessage(ConstraintValidatorContext context, String messageCode, Object[] params) {
        String message = messageSource.getMessage(messageCode, params, LocaleContextHolder.getLocale());
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }

    @Override
    public void initialize(ValidateMultipartFile constraintAnnotation) {
        this.extensions = constraintAnnotation.extensions();
        this.maxSize = constraintAnnotation.maxSize();
    }


    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        if (value != null) {
            String fileExtension = StringUtil.getFileExtension(value.getOriginalFilename());
            for (String ext : extensions) {
                if (!ext.equals(fileExtension)) {
                    //set message
                    baseService.setMessage(context, "system.multipart.extensions.message", new Object[] { "{0}",
                            Arrays.stream(extensions).toList().toString() });
                    return false;
                }
            }
            if (!MultipartUtil.checkSize(value, maxSize)) {
                //set message
                baseService.setMessage(context, "system.multipart.size.message", new Object[] { "{0}", maxSize });
                return false;
            }

        }
        return true;
    }
}
