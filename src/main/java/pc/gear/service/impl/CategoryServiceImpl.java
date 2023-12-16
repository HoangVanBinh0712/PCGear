package pc.gear.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pc.gear.entity.Category;
import pc.gear.repository.CategoryRepository;
import pc.gear.request.category.AddCategoryRequest;
import pc.gear.request.category.DeleteCategoryRequest;
import pc.gear.request.category.UpdateCategoryRequest;
import pc.gear.response.category.GetCategoryResponse;
import pc.gear.service.CategoryService;
import pc.gear.validator.CategoryValidator;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CategoryValidator categoryValidator;

    @Override
    public void addCategory(AddCategoryRequest request) {
        categoryValidator.validateAddCategory(request);
        Category category = modelMapper.map(request, Category.class);
        categoryRepository.save(category);
    }

    @Override
    public void updateCategory(UpdateCategoryRequest request) {
        Category category = categoryValidator.validateUpdateCategory(request);
        category.setCategoryCd(request.getCategoryCd());
        category.setName(request.getName());
        category.setDeleteFlag(request.getDeleteFlag());
        category.setDescription(request.getDescription());
        categoryRepository.save(category);
    }

    @Override
    public GetCategoryResponse getCategory() {
        // Not delete only
        return GetCategoryResponse.builder().categories(categoryRepository.findByDeleteFlagIsNullOrDeleteFlagEquals(Boolean.FALSE)).build();
    }

    @Override
    public void delete(DeleteCategoryRequest request) {
        List<Category> categories = categoryValidator.validateDelete(request);
        categoryRepository.deleteAll(categories);
    }

    @Override
    public GetCategoryResponse getCategoryAll() {
        return GetCategoryResponse.builder().categories(categoryRepository.findAll()).build();
    }
}
