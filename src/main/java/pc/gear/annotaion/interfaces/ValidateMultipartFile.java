package pc.gear.annotaion.interfaces;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import pc.gear.annotaion.validators.ValidMultipartFileValidator;
import pc.gear.util.Constants;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = ValidMultipartFileValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateMultipartFile {
    String message() default Constants.EMPTY;

    String[] extensions() default {};

    // Mb
    int maxSize() default 10;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
