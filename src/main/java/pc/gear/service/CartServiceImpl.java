package pc.gear.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pc.gear.entity.Cart;
import pc.gear.repository.CartRepository;
import pc.gear.repository.CustomerRepository;
import pc.gear.request.cart.AddToCartRequest;
import pc.gear.request.cart.DeleteCartRequest;
import pc.gear.request.cart.UpdateCartRequest;
import pc.gear.response.Cart.GetCartResponse;
import pc.gear.service.impl.CartService;
import pc.gear.util.JwtUtil;
import pc.gear.util.NumberUtil;
import pc.gear.validator.CartValidator;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartValidator cartValidator;

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    @Transactional
    public void addToCart(AddToCartRequest request) {
        Cart cart = cartValidator.validateAddToCart(request);
        if (cart.getCartId() == null) {
            cart.setQuantity(request.getQuantity());
            cart.setCustomer(customerRepository.getReferenceById(JwtUtil.getCurrentUserId()));
        } else {
            // Increase the quantity
            Integer quantity = NumberUtil.add2Integer(cart.getQuantity(), request.getQuantity());
            cart.setQuantity(quantity == null ? request.getQuantity() : quantity);
        }
        cartRepository.save(cart);
    }



    @Override
    @Transactional
    public void updateCart(UpdateCartRequest request) {
        Cart cart = cartValidator.validateUpdateCart(request);
        cart.setQuantity(request.getQuantity());
        cartRepository.save(cart);
    }

    @Override
    @Transactional
    public void deleteCart(DeleteCartRequest request) {
        cartRepository.deleteByCartIds(request.getCartIds());
    }

    @Override
    public GetCartResponse getCartItem() {
        return GetCartResponse.builder()
                .carts(cartRepository.getCartItems())
                .build();
    }

}
