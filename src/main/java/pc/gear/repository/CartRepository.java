package pc.gear.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import pc.gear.entity.Cart;
import pc.gear.repository.custom.CartCustomizedRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long>, CartCustomizedRepository {

    @Query(value = """
            select c from Cart c where c.cartId = :cartId and c.customer.customerId = :customerId
                """)
    List<Cart> findByCartIdAndCustomerId(Long cartId, Long customerId);

    @Query(value = """
            select c from Cart c where c.product.productId = :productId and c.customer.customerId = :customerId
                """)
    List<Cart> findByProductIdAndCustomerId(Long productId, Long customerId);

    @Modifying
    @Query(value = """
            delete Cart c where c.cartId in (:ids)
                """)
    void deleteByCartIds(List<Long> ids);
}
