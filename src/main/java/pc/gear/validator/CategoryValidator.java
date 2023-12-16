package pc.gear.validator;

import pc.gear.entity.Category;
import pc.gear.request.category.AddCategoryRequest;
import pc.gear.request.category.DeleteCategoryRequest;
import pc.gear.request.category.UpdateCategoryRequest;

import java.util.List;

public interface CategoryValidator {
    void validateAddCategory(AddCategoryRequest request);

    Category validateUpdateCategory(UpdateCategoryRequest request);

    List<Category> validateDelete(DeleteCategoryRequest request);
}
