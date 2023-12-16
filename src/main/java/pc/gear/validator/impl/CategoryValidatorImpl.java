package pc.gear.validator.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pc.gear.entity.Category;
import pc.gear.repository.CategoryRepository;
import pc.gear.request.category.AddCategoryRequest;
import pc.gear.request.category.DeleteCategoryRequest;
import pc.gear.request.category.UpdateCategoryRequest;
import pc.gear.service.BaseService;
import pc.gear.util.MessageConstants;
import pc.gear.validator.CategoryValidator;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryValidatorImpl extends BaseService implements CategoryValidator {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public void validateAddCategory(AddCategoryRequest request) {
        // Check exist cd in database
        if (Boolean.TRUE.equals(categoryRepository.existsByCategoryCd(request.getCategoryCd()))) {
            // throw message
            this.throwError(MessageConstants.DUPLICATE_VALUE, request.getCategoryCd());
        }
    }

    @Override
    public Category validateUpdateCategory(UpdateCategoryRequest request) {
        Optional<Category> optCategory = categoryRepository.findById(request.getCategoryId());
        if (optCategory.isEmpty()) {
            this.throwError(MessageConstants.DATA_NOT_FOUND, this.getMessage(MessageConstants.CATEGORY));
        }
        Category category = optCategory.get();
        if (!StringUtils.equals(request.getCategoryCd(), category.getCategoryCd())) {
            validateAddCategory(request);
        }
        return category;
    }

    @Override
    public List<Category> validateDelete(DeleteCategoryRequest request) {
        return categoryRepository.findAllById(request.getCategoryIds());
    }

}
