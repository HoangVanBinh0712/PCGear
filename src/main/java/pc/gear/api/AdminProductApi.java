package pc.gear.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pc.gear.request.admin.product.CreateProductRequest;
import pc.gear.request.admin.product.UpdateProductRequest;
import pc.gear.service.AdminProductService;
import pc.gear.util.Constants;
import pc.gear.util.UriConstants;
import pc.gear.util.response.ApiResponse;

@RestController
@RequestMapping(value = UriConstants.ADMIN + UriConstants.PRODUCT)
public class AdminProductApi {

    @Autowired
    private AdminProductService adminProductService;

    @Operation(summary = "Add Product", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping
    private ApiResponse<?> create(@RequestBody @Valid CreateProductRequest request){
        adminProductService.create(request);
        return new ApiResponse<>();
    }

    @Operation(summary = "Update Product", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping
    private ApiResponse<?> update(@RequestBody @Valid UpdateProductRequest request){
        adminProductService.update(request);
        return new ApiResponse<>();
    }

    @Operation(summary = "Delete Product", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping
    private ApiResponse<?> delete(@RequestParam(value = Constants.PROJECT_CODE, required = false) String projectCode) {
        adminProductService.delete(projectCode);
        return new ApiResponse<>();
    }
}
