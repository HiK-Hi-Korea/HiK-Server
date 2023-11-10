package HiK.HiKServer.User.repository;

import HiK.HiKServer.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

}
