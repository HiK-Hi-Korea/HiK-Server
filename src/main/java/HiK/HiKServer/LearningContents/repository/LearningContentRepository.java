package HiK.HiKServer.LearningContents.repository;

import HiK.HiKServer.entity.LearningContent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LearningContentRepository extends JpaRepository<LearningContent, Long> {
}
