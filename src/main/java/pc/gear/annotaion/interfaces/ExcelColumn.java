package pc.gear.annotaion.interfaces;

import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelColumn {
    String message() default "excel.message";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String sheetName();

    String columnName();

    String fieldName();

}
