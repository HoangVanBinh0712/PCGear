package pc.gear.annotaion.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pc.gear.annotaion.interfaces.TextArea;
import pc.gear.util.lang.StringUtil;


public class TextAreaValidator implements ConstraintValidator<TextArea, String> {

    private int max;
    private int min;

    @Override
    public void initialize(TextArea constraintAnnotation) {
        this.max = constraintAnnotation.max();
        this.min = constraintAnnotation.min();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return StringUtil.validateTextArea(value, min, max);
    }

}
