package pc.gear.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pc.gear.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
