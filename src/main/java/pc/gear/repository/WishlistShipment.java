package pc.gear.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pc.gear.entity.Wishlist;

public interface WishlistShipment extends JpaRepository<Wishlist, Long> {
}
