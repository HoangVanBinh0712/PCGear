package pc.gear.annotaion.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import pc.gear.annotaion.interfaces.EnumValue;
import pc.gear.annotaion.interfaces.IntegerNumber;
import pc.gear.util.lang.NumberUtil;

import java.math.BigDecimal;


public class IntegerNumberValidator implements ConstraintValidator<IntegerNumber, Number> {

    private int max;

    @Override
    public void initialize(IntegerNumber constraintAnnotation) {
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(Number number, ConstraintValidatorContext constraintValidatorContext) {
        return NumberUtil.validateIntegerNumber(number, max);
    }



}
