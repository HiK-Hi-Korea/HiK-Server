package HiK.HiKServer.User.repository;

import HiK.HiKServer.User.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
//    boolean existsByLoginId(String loginId);
//    boolean existsByNickname(String nickname);
//    Optional<User> findByLoginId(String loginId);
}
