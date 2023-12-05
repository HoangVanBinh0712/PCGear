package pc.gear.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pc.gear.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
