package pc.gear.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pc.gear.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
