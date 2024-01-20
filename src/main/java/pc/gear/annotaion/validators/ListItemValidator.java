package pc.gear.annotaion.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import pc.gear.annotaion.interfaces.ListItem;
import pc.gear.util.lang.NumberUtil;
import pc.gear.util.lang.StringUtil;

import java.util.List;

public class ListItemValidator implements ConstraintValidator<ListItem, List<?>> {

    @Autowired
    private MessageSource messageSource;

    private int minLength;

    private int maxLength;

    private int integerPart;

    private int fractionPart;

    private boolean isCheckString;

    private boolean isCheckNumber;

    private int listSize;

    @Override
    public void initialize(ListItem constraintAnnotation) {
        this.minLength = constraintAnnotation.minLength();
        this.maxLength = constraintAnnotation.maxLength();
        this.integerPart = constraintAnnotation.integerPart();
        this.fractionPart = constraintAnnotation.fractionPart();
        this.isCheckString = constraintAnnotation.isCheckString();
        this.isCheckNumber = constraintAnnotation.isCheckNumber();
        this.listSize = constraintAnnotation.listSize();
    }

    @Override
    public boolean isValid(List<?> value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Null values are considered valid
        }
        // Validate size of list
        if (value.size() > listSize) {
            setMessage(context, "system.list.item.size.listSize.message", new Object[] { "{0}", String.valueOf(listSize) });
            return false;
        }
        if (isCheckString) {
            for (Object item : value) {
                if (!StringUtil.checkSize(item, minLength, maxLength)) {
                    // Set message
                    setMessage(context, "system.list.item.size.string.message", new Object[] { "{0}", minLength, maxLength });
                    return false;
                }
            }
        }
        if (isCheckNumber) {
            for (Object item : value) {
                if (!NumberUtil.isValidNumber(item, integerPart, fractionPart)) {
                    Object[] params = new Object[] { "{0}", StringUtil.generateMessageDigits(integerPart, fractionPart) };
                    setMessage(context, "system.list.item.size.number.message", params);
                    return false;
                }
            }
        }

        return true;
    }

    private void setMessage(ConstraintValidatorContext context, String messageCode, Object[] params) {
        String message = messageSource.getMessage(messageCode, params, LocaleContextHolder.getLocale());
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
    }
}
