package HiK.HiKServer.repository;

import HiK.HiKServer.User.repository.UserRepository;
import HiK.HiKServer.User.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void testSaveAndFind() {
        List<User> all = userRepository.findAll();
        System.out.println("--------------------------");
        for(User user: all){
            System.out.println(user.getId());
        }
        System.out.println("--------------------------");

        User foundUser = userRepository.findById("1").orElse(null);
        // then
        assertNotNull(foundUser);
        // assertNotNull(all);
    }
}
