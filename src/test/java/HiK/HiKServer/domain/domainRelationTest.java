package HiK.HiKServer.domain;

import HiK.HiKServer.LearningContents.repository.LearningContentRepository;
import HiK.HiKServer.Translator.repositroy.SentenceRepository;
import HiK.HiKServer.User.repository.UserRepository;
import HiK.HiKServer.entity.Sentence;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class domainRelationTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    LearningContentRepository learningContentRepository;
    @Autowired
    SentenceRepository sentenceRepository;

    @Test
    void relationshiptTest(){
        Sentence sentence = new Sentence();
    }
}
