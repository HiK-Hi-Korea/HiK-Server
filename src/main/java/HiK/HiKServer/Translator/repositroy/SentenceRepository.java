package HiK.HiKServer.Translator.repositroy;

import HiK.HiKServer.entity.Sentence;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SentenceRepository extends CrudRepository<Sentence, Long> {
    @Query(value = "SELECT * FROM Sentence s WHERE s.userId = :userId AND s.place = :place AND TIMESTAMPDIFF(MINUTE, s.timestamp, :timestamp) BETWEEN -5 AND 5", nativeQuery = true)
    List<Sentence> findSimilarSentences(@Param("user") String userId, @Param("place") String place, @Param("timestamp") LocalDateTime timestamp);
}