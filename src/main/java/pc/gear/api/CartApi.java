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
import pc.gear.request.cart.AddToCartRequest;
import pc.gear.request.cart.DeleteCartRequest;
import pc.gear.request.cart.UpdateCartRequest;
import pc.gear.service.impl.CartService;
import pc.gear.util.UriConstants;
import pc.gear.util.response.ApiResponse;

@RestController
@RequestMapping(value = UriConstants.CART)
public class CartApi {
    @Autowired
    private CartService cartService;

    @Operation(summary = "Get Cart", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping
    private ApiResponse<?> getCartItem() {
        return new ApiResponse<>(cartService.getCartItem());
    }

    @Operation(summary = "Add to Cart", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping
    private ApiResponse<?> addToCart(@RequestBody @Valid AddToCartRequest request) {
        cartService.addToCart(request);
        return new ApiResponse<>();
    }

    @Operation(summary = "Update Cart", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping
    private ApiResponse<?> updateCart(@RequestBody @Valid UpdateCartRequest request) {
        cartService.updateCart(request);
        return new ApiResponse<>();
    }

    @Operation(summary = "Delete Cart", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping
    private ApiResponse<?> updateCart(@RequestBody @Valid DeleteCartRequest request) {
        cartService.deleteCart(request);
        return new ApiResponse<>();
    }
}
