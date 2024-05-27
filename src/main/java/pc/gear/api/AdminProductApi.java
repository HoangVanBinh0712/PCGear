package pc.gear.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pc.gear.request.admin.product.CreateProductRequest;
import pc.gear.request.admin.product.ExportProductRequest;
import pc.gear.request.admin.product.ImportProductRequest;
import pc.gear.request.admin.product.UpdateProductRequest;
import pc.gear.service.AdminProductService;
import pc.gear.util.Constants;
import pc.gear.util.UriConstants;
import pc.gear.util.response.ApiResponse;

import java.io.IOException;
import java.sql.SQLException;

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
    private ApiResponse<?> delete(@RequestParam(value = Constants.PRODUCT_CODE, required = false) String projectCode) {
        adminProductService.delete(projectCode);
        return new ApiResponse<>();
    }

    @Operation(summary = "Import Product", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    private ApiResponse<?> importProduct(@ModelAttribute @Valid ImportProductRequest request) throws IOException, IllegalAccessException, SQLException {
        adminProductService.importProduct(request);
        return new ApiResponse<>();
    }

    @Operation(summary = "Export Product", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "export")
    private ApiResponse<?> exportProduct(@RequestBody @Valid ExportProductRequest request) throws IOException, IllegalAccessException, SQLException {
        adminProductService.exportProduct(request);
        return new ApiResponse<>();
    }
}
