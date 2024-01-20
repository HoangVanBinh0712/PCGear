package pc.gear.annotaion.interfaces;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import pc.gear.annotaion.validators.ListItemValidator;
import pc.gear.util.Constants;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = ListItemValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ListItem {
    String message() default Constants.EMPTY;

    int minLength() default 0;

    int maxLength() default Integer.MAX_VALUE;

    boolean isCheckString() default false;

    boolean isCheckNumber() default false;

    int integerPart() default Integer.MAX_VALUE;

    int fractionPart() default Integer.MAX_VALUE;

    int listSize() default Integer.MAX_VALUE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
