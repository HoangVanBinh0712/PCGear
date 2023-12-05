package pc.gear.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pc.gear.entity.Shipment;

public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
}
