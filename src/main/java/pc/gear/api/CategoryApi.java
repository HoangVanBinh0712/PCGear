package pc.gear.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pc.gear.request.category.AddCategoryRequest;
import pc.gear.request.category.DeleteCategoryRequest;
import pc.gear.request.category.SearchCategoryRequest;
import pc.gear.request.category.UpdateCategoryRequest;
import pc.gear.response.category.GetCategoryResponse;
import pc.gear.response.category.SearchCategoryResponse;
import pc.gear.service.CategoryService;
import pc.gear.util.UriConstants;
import pc.gear.util.response.ApiResponse;

@RestController
@RequestMapping(UriConstants.CATEGORY)
public class CategoryApi {

    @Autowired
    private CategoryService categoryService;

    /**
     * Add category
     *
     * @param request
     * @return ApiResponse
     * @author BinhSenpai
     */
    @Operation(summary = "Add category", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping
    public ApiResponse<?> addCategory(@RequestBody @Valid AddCategoryRequest request) {
        categoryService.addCategory(request);
        return new ApiResponse<>();
    }

    /**
     * Update category
     *
     * @param request
     * @return ApiResponse
     * @author BinhSenpai
     */
    @Operation(summary = "Update category", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping
    public ApiResponse<?> updateCategory(@RequestBody @Valid UpdateCategoryRequest request) {
        categoryService.updateCategory(request);
        return new ApiResponse<>();
    }

    /**
     * Get category
     *
     * @return ApiResponse
     * @author BinhSenpai
     */
    @Operation(summary = "Get category", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping
    public ApiResponse<GetCategoryResponse> getCategory() {
        return new ApiResponse<>(categoryService.getCategory());
    }

    /**
     * Get category
     *
     * @return ApiResponse
     * @author BinhSenpai
     */
    @Operation(summary = "Get category by text", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = UriConstants.SEARCH)
    public ApiResponse<SearchCategoryResponse> getCategoryByText(SearchCategoryRequest request) {
        return new ApiResponse<>(categoryService.search(request));
    }

    /**
     * Get category (Admin use only)
     *
     * @return ApiResponse
     * @author BinhSenpai
     */
    @Operation(summary = "Get category", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = UriConstants.ALL)
    public ApiResponse<GetCategoryResponse> getCategoryAll() {
        return new ApiResponse<>(categoryService.getCategoryAll());
    }


    /**
     * Delete category
     *
     * @return ApiResponse
     * @author BinhSenpai
     */
    @Operation(summary = "Delete category", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping
    public ApiResponse<?> delete(@RequestBody DeleteCategoryRequest request) {
        categoryService.delete(request);
        return new ApiResponse<>();
    }
}
