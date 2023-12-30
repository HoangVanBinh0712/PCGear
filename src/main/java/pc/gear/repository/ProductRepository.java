package pc.gear.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pc.gear.entity.Product;
import pc.gear.repository.custom.ProductCustomRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductCustomRepository {

    Product findByProductCode(String productCode);

    @Query(value = """
            select p from Product p where p.productId in :ids and (p.deleteFlag is null or p.deleteFlag = false)
            """)
    List<Product> findByProductIdInAAndDeleteFlagIsFalse(List<Long> ids);
}
