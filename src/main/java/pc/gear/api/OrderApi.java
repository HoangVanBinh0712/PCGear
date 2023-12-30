package pc.gear.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pc.gear.request.Order.CreateOrderRequest;
import pc.gear.service.OrderService;
import pc.gear.util.UriConstants;
import pc.gear.util.response.ApiResponse;

@RestController
@RequestMapping(value = UriConstants.ORDER)
public class OrderApi {
    @Autowired
    private OrderService orderService;

    @Operation(summary = "Create order", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping
    private ApiResponse<?> create(@RequestBody @Valid CreateOrderRequest request) {
        orderService.create(request);
        return new ApiResponse<>();
    }

}
