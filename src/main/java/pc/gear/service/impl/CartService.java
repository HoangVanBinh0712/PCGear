package pc.gear.service.impl;

import pc.gear.request.cart.AddToCartRequest;
import pc.gear.request.cart.DeleteCartRequest;
import pc.gear.request.cart.UpdateCartRequest;
import pc.gear.response.Cart.GetCartResponse;

public interface CartService {
    void addToCart(AddToCartRequest request);

    void updateCart(UpdateCartRequest request);

    void deleteCart(DeleteCartRequest request);

    GetCartResponse getCartItems();
}
