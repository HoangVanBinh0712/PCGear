package pc.gear.annotaion.interfaces;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import pc.gear.annotaion.validators.IntegerNumberValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = IntegerNumberValidator.class)
public @interface IntegerNumber {
    String message() default "IntegerNumber.message";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int max();
}
