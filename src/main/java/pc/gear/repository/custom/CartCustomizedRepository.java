package pc.gear.repository.custom;

import pc.gear.response.Cart.GetCartResponse;

import java.util.List;

public interface CartCustomizedRepository {

    List<GetCartResponse.CartItem> getCartItems();
}
