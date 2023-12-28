package pc.gear.validator;

import pc.gear.entity.Cart;
import pc.gear.request.cart.AddToCartRequest;
import pc.gear.request.cart.UpdateCartRequest;

public interface CartValidator {
    Cart validateAddToCart(AddToCartRequest request);

    Cart validateUpdateCart(UpdateCartRequest request);
}
