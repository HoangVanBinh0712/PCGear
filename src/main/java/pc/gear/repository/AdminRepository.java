package pc.gear.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pc.gear.entity.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    Admin findTop1ByUserName(String userName);
}
