package pc.gear.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pc.gear.entity.Customer;
import pc.gear.repository.custom.OrderCustomizedRepository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>, OrderCustomizedRepository {
    Customer findTop1ByUserName(String username);
}
