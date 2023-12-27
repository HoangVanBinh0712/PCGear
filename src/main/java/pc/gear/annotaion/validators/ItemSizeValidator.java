package pc.gear.annotaion.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.CollectionUtils;
import pc.gear.annotaion.interfaces.ItemSize;

import java.util.List;

/**
 * Validate an item of a String array is valid size
 *
 * @author BinhSenpai
 */
public class ItemSizeValidator implements ConstraintValidator<ItemSize, List<String>> {
    private int min;
    private int max;

    @Override
    public void initialize(ItemSize constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(List<String> value, ConstraintValidatorContext context) {
        if (!CollectionUtils.isEmpty(value)) {
            for (String item : value) {
                if (item != null && (min > item.length() || max < item.length())) {
                    return false;
                }
            }
        }
        return true;
    }
}
