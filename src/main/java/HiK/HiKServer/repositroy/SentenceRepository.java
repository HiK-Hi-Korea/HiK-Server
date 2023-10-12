package HiK.HiKServer.repositroy;

import HiK.HiKServer.entity.Sentence;
import org.springframework.data.repository.CrudRepository;

public interface SentenceRepository extends CrudRepository<Sentence, Long> {
}
