package HiK.HiKServer.LearningContents.repository;

import HiK.HiKServer.LearningContents.domain.LearningContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface LearningContentRepository extends JpaRepository<LearningContent, Long> {

    @Query("SELECT lc FROM LearningContent lc WHERE lc.user.id = :userId")
    List<LearningContent> getByUserId(@Param("userId") String userId);

    @Query("SELECT lc FROM LearningContent lc WHERE lc.user.id = :userId AND lc.place = :place AND lc.listener = :listener AND lc.intimacy = :intimacy AND lc.timestamp <= :start AND lc.timestamp >= :end")
    LearningContent findSimilarContents(@Param("userId") String userId, @Param("place") String place, @Param("listener") String listener, @Param("intimacy") int intimacy, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

}
