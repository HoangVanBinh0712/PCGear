package pc.gear.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pc.gear.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
