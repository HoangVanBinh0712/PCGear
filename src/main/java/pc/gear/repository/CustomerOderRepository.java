package pc.gear.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pc.gear.entity.CustomerOrder;

public interface CustomerOderRepository extends JpaRepository<CustomerOrder, Long> {
}
