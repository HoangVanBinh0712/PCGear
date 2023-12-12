package pc.gear.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pc.gear.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    Department findByDepartmentCd(String departmentCd);
}
