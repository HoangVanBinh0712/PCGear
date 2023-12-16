package pc.gear.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pc.gear.entity.Category;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByDeleteFlagIsNullOrDeleteFlagEquals(Boolean deleteFg);
    Boolean existsByCategoryCd(String categoryCd);
    Boolean existsByCategoryIdAndDeleteFlagNot(Long categoryId, Boolean deleteFg);
}
