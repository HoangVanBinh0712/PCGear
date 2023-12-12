package pc.gear.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pc.gear.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findTop1ByUserName(String username);
}
