package pc.gear.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pc.gear.entity.Product;
import pc.gear.repository.custom.ProductCustomRepository;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductCustomRepository {

    Product findByProductCode(String productCode);
}
