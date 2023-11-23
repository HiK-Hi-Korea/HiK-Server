package HiK.HiKServer.repository;

import HiK.HiKServer.LearningContents.repository.LearningContentRepository;
import HiK.HiKServer.LearningContents.domain.LearningContent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class LearningContentsTest {
    @Autowired
    private LearningContentRepository learningContentRepository;

    @Test
    public void findSimilarSentenceTest(){
        List<LearningContent> data = learningContentRepository.findAll();
        System.out.println("-----------------");
        for (LearningContent d: data){
            System.out.println("learning Content: "+ d.toString());
        }
        System.out.println("-----------------");

        assertNotNull(data);
    }
}