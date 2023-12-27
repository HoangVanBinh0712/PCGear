package pc.gear.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pc.gear.dto.interfaces.ICategorySearch;
import pc.gear.entity.Category;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByDeleteFlagIsNullOrDeleteFlagEquals(Boolean deleteFg);
    Boolean existsByCategoryCd(String categoryCd);
    Boolean existsByCategoryIdAndDeleteFlagNot(Long categoryId, Boolean deleteFg);

    @Query(value = """
            select c.category_id as categoryId,
                   c.name,
                   c.created_datetime as createdDateTime,
                   c.created_by as createdBy,
                   c.updated_datetime as updatedDateTime,
                   c.updated_by as updatedBy,
                   c.category_cd as categoryCd,
                   c.delete_fg as deleteFlag,
                   c.description
            from category c
            where (c.name like :text or description like :text)
                               """, nativeQuery = true)
    List<ICategorySearch> searchCategory(@Param("text") String text, Pageable pageable);
}
