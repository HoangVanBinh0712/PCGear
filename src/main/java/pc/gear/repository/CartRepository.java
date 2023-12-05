package pc.gear.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pc.gear.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
