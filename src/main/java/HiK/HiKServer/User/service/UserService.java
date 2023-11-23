package HiK.HiKServer.User.service;

import HiK.HiKServer.User.repository.UserRepository;
import HiK.HiKServer.LearningContents.domain.LearningContent;
import HiK.HiKServer.User.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<LearningContent> getLearningContentsByUserId(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return user.getLearningContentList();
    }
}