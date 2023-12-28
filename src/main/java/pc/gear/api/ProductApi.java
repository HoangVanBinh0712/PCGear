package pc.gear.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pc.gear.request.product.ProductSearchRequest;
import pc.gear.service.ProductService;
import pc.gear.util.Constants;
import pc.gear.util.UriConstants;
import pc.gear.util.response.ApiResponse;

@RestController
@RequestMapping(value = UriConstants.PRODUCT)
public class ProductApi {

    @Autowired
    private ProductService productService;

    @Operation(summary = "Get Product by code", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = UriConstants.PRODUCT_BY_CODE)
    private ApiResponse<?> get(@RequestParam(value = Constants.PRODUCT_CODE, required = false) String projectCode){
        return new ApiResponse<>(productService.getByProductCode(projectCode));
    }


    @Operation(summary = "Search Product", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = UriConstants.SEARCH)
    private ApiResponse<?> search(@RequestBody @Valid ProductSearchRequest request) {
        return new ApiResponse<>(productService.search(request));
    }
}
