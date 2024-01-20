package pc.gear.annotaion.interfaces;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import pc.gear.annotaion.validators.TextAreaValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = TextAreaValidator.class)
public @interface TextArea {
    String message() default "{jakarta.validation.constraints.Size.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int max() default Integer.MAX_VALUE;
    int min() default 0;
}
