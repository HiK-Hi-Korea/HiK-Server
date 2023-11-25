package HiK.HiKServer.LearningContents.repository;

import HiK.HiKServer.LearningContents.domain.LearningContent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LearningContentRepository extends JpaRepository<LearningContent, Long> {

    public LearningContent findSimilarContent()
}
