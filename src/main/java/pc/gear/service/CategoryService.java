package pc.gear.service;

import pc.gear.request.category.AddCategoryRequest;
import pc.gear.request.category.DeleteCategoryRequest;
import pc.gear.request.category.SearchCategoryRequest;
import pc.gear.request.category.UpdateCategoryRequest;
import pc.gear.response.category.GetCategoryResponse;
import pc.gear.response.category.SearchCategoryResponse;

public interface CategoryService {
    void addCategory(AddCategoryRequest request);

    void updateCategory(UpdateCategoryRequest request);

    GetCategoryResponse getCategory();

    void delete(DeleteCategoryRequest request);

    GetCategoryResponse getCategoryAll();

    SearchCategoryResponse search(SearchCategoryRequest request);
}
