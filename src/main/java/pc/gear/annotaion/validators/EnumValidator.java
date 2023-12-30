package pc.gear.annotaion.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import pc.gear.annotaion.interfaces.EnumValue;


public class EnumValidator implements ConstraintValidator<EnumValue, String> {

    private Class<? extends Enum<?>> enumClass;
    private String message;

    @Autowired
    MessageSource messageSource;

    @Override
    public void initialize(EnumValue constraintAnnotation) {
        this.enumClass = constraintAnnotation.enumClass();
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;  // null values are handled by other validators or annotations
        }

        try {
            Enum<?>[] enumConstants = enumClass.getEnumConstants();
            for (Enum<?> enumConstant : enumConstants) {
                if (enumConstant.name().equalsIgnoreCase(value)) {
                    return true;
                }
            }

            String className = enumClass.getSimpleName();
            String enumValues = getEnumValuesAsString(enumConstants);
            String newMessaage = messageSource.getMessage(
                    message,
                    new Object[]{className, enumValues},
                    LocaleContextHolder.getLocale()
            );
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(newMessaage).addConstraintViolation();
            return false;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
    private String getEnumValuesAsString(Enum<?>[] enumConstants) {
        StringBuilder valuesAsString = new StringBuilder();
        for (Enum<?> enumConstant : enumConstants) {
            valuesAsString.append("'").append(enumConstant.name()).append("',");
        }
        return valuesAsString.substring(0, valuesAsString.length() - 2);
    }
}
