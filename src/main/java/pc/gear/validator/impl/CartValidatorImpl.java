package pc.gear.validator.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pc.gear.entity.Cart;
import pc.gear.entity.Product;
import pc.gear.repository.CartRepository;
import pc.gear.repository.ProductRepository;
import pc.gear.request.cart.AddToCartRequest;
import pc.gear.request.cart.UpdateCartRequest;
import pc.gear.service.BaseService;
import pc.gear.util.lang.CollectionUtil;
import pc.gear.util.lang.JwtUtil;
import pc.gear.util.MessageConstants;
import pc.gear.validator.CartValidator;

import java.util.Optional;

@Service
public class CartValidatorImpl extends BaseService implements CartValidator {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartRepository cartRepository;

    @Override
    public Cart validateAddToCart(AddToCartRequest request) {
        // Check that the product is available (not deleted)
        Optional<Product> p = productRepository.findById(request.getProductId());
        if (p.isPresent() && !Boolean.TRUE.equals(p.get().getDeleteFlag())) {
            // Check if user alread add this item to cart => Just increase the quantity
            Cart cart = CollectionUtil.getFirstFormCollection(cartRepository.findByProductIdAndCustomerId(request.getProductId(), JwtUtil.getCurrentUserId()));
            if (cart == null) {
                cart = new Cart();
            }
            cart.setProduct(p.get());
            return cart;
        }
        this.throwError(MessageConstants.DATA_NOT_FOUND, this.getMessage(MessageConstants.PRODUCT));
        return null;
    }

    @Override
    public Cart validateUpdateCart(UpdateCartRequest request) {
        // Check that cart is of the user
        Cart cart = CollectionUtil.getFirstFormCollection(cartRepository.findByCartIdAndCustomerId(request.getCartId(), JwtUtil.getCurrentUserId()));
        if (cart == null) {
            this.throwError(MessageConstants.DATA_NOT_FOUND, this.getMessage(MessageConstants.CART));
        }
        return cart;
    }
}
