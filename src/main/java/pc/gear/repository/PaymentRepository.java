package pc.gear.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pc.gear.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
